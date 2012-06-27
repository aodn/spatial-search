package au.org.emii.search.index

import java.sql.PreparedStatement

import org.apache.http.client.HttpResponseException;
import org.hibernate.LockMode
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate

import au.org.emii.search.FeatureType

class FeatureTypeRequestService {

    static transactional = false
	
	def grailsApplication
	def sessionFactory
	def mailService
	def dataSource
	def geoNetworkSearchSummaryCache
	def propertyInstanceMap = org.codehaus.groovy.grails.plugins.DomainClassGrailsPlugin.PROPERTY_INSTANCE_MAP
	
    def index() {
		def featureCount = 0
		def messages = []
		def indexRun = new IndexRun()
		def metadataRecords = _loadGeonetworkMetadata()
		log.info("Loaded ${metadataRecords.size()} metadata records for indexing")
		
		def servers = _toFeatureServiceServers(metadataRecords)
		servers.values().each { server ->
			server.featureTypeUuids.each { featureTypeName, metadataList ->
				try {
					featureCount += _index(indexRun, metadataList, messages)
				}
				catch (HttpResponseException e) {
					messages << "Error ${server.url} not indexed due to http issue: ${e.toString()}\nCheck logs for more detail."
				}
				catch (Exception e) {
					log.error("", e)
					messages << "Error ${server.url} not indexed, could not retrieve features: ${e.toString()}"
				}
			}
		}
		
		if (featureCount > 0) {
			_clearCache()
		}
		
		log.info("Finished index run $indexRun")
		_sendMail(messages)
		
		return featureCount
    }
	
	def _index(indexRun, metadataRecords, messages) {
		def featureCount = 0
		
		if (indexRun.geonetworkMetadataDocs.size() > 0) {
			_reattach(indexRun)
		}
		
		def metadata = metadataRecords[0]
		def featureTypeRequestImpl = _getFeatureTypeRequestImplementation(metadata, indexRun, messages)
		def features = new ArrayList(featureTypeRequestImpl.requestFeatureType(metadata))
		if (!features.isEmpty()) {
			try {
				_addFeaturesForOtherMetadata(metadata, features, metadataRecords)
				_saveMetadataFeatures(metadata, features)
				_updateIndexRun(indexRun, metadataRecords)
				featureCount = features.size()
			}
			catch (Exception e) {
				// Features related to this metadata could not be saved
				messages << "Error ${metadata.geoserverEndPoint} not indexed: ${e.toString()}"
			}
		}
		return featureCount
	}
	
	def _loadGeonetworkMetadata() {
		def metadataRecords = GeonetworkMetadata.findAllByIndexRunIsNull()
		// Piff them out of the main session transaction so when modified by the
		// async call they can be reloaded here
		// There is no async call now but it's worth leaving this code in
		metadataRecords*.discard()
	}

	def _updateIndexRun(indexRun, metadataList) {
		indexRun.geonetworkMetadataDocs.addAll(metadataList)
		metadataList*.indexRun = indexRun
		/*
		 * This looks stupid and inefficient but if save is not called on
		 * IndexRun here an exception relating to a transient object is
		 * thrown
		 */
		_save(indexRun)
	}
	
	def _saveMetadataFeatures(metadata, features) {
		def featureCount = features.size()
		def index = 0
		def sliceSize = _getSliceSize()
		while (index < featureCount) {
			def sliceEnd = index + (index + sliceSize < featureCount ? sliceSize : featureCount - index)
			log.debug("Slicing from $index to " + sliceEnd)
			def slice = features.subList(index, sliceEnd)
			try {
				_saveFeatures(metadata, slice)
			}
			finally {
				_clearGorm()
			}
			// Now evict the features from the session in the hope
			// the GC will pick them up
			//features*.discard()
			index = sliceEnd
		}
	}
	
	def _saveFeatures(metadata, features) {
		def featuresToPersist = []
		def uuids = features.collect { feature -> feature.geonetworkUuid }.unique()
		def featureTypeIds = features.collect { feature -> feature.featureTypeId }.unique()
		
		def persistedFeatures = fetchFeatureTypes(metadata.featureTypeName, uuids, featureTypeIds)
		def persistedFeaturesMap = _mapFeatureByFeatureTypeId(persistedFeatures)
		features.each() { feature ->
			def featureToPersist = persistedFeaturesMap[feature.featureTypeId]
			if (featureToPersist) {
				featureToPersist.gml = feature.gml
			}
			else {
				featureToPersist = feature
			}
			featuresToPersist << featureToPersist
		}
		_jdbcBatchSaveFeatures(featuresToPersist)
	}
	
	def _jdbcBatchSaveFeatures(features) {
		def inserts = []
		def updates = []
		
		features.each { feature ->
			if (feature.id) {
				updates << feature
			}
			else {
				inserts << feature
			}
		}
		
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource)
		_doFeaturesJdbcBatchUpdate(jdbcTemplate, updates)
		_doFeaturesJdbcBatchInsert(jdbcTemplate, inserts)
	}
	
	def _doFeaturesJdbcBatchInsert(jdbcTemplate, featuresToInsert) {
		if (!featuresToInsert.isEmpty()) {
			try {
				jdbcTemplate.batchUpdate(
					"insert into feature_type (id, version, feature_type_id, feature_type_name, geonetwork_uuid, geometry) values (nextval('hibernate_sequence'), 0, ?, ?, ?, st_geomfromgml(?))", 
					_getFeaturesJdbcBatchInsertStatementSetter(featuresToInsert)
				)
			}
			catch (DataAccessException e) {
				log.error('', e)
				throw e
			}
			catch (Exception e) {
				log.error('', e)
				throw e
			}
		}
	}
	
	def _doFeaturesJdbcBatchUpdate(jdbcTemplate, featuresToUpdate) {
		if (!featuresToUpdate.isEmpty()) {
			try {
				jdbcTemplate.batchUpdate(
					"update feature_type set version = version + 1, feature_type_id = ?, feature_type_name = ?, geonetwork_uuid = ?, geometry = st_geomfromgml(?) where id = ?",
					_getFeaturesJdbcBatchUpdateStatementSetter(featuresToUpdate)
				)
			}
			catch (DataAccessException e) {
				log.error('', e)
				throw e
			}
			catch (Exception e) {
				log.error('', e)
				throw e
			}
		}
	}
	
	def _getFeaturesJdbcBatchInsertStatementSetter(featuresToInsert) {
		return new BatchPreparedStatementSetter() {
			def inserts = featuresToInsert
			
			int getBatchSize() {
				return inserts.size();
			}
			
			void setValues(PreparedStatement ps, int i) {
				def feature = inserts[i]
				int j = 1
				ps.setString(j++, feature.featureTypeId)
				ps.setString(j++, feature.featureTypeName)
				ps.setString(j++, feature.geonetworkUuid)
				ps.setString(j++, feature.gml)
			}
		}
	}
	
	def _getFeaturesJdbcBatchUpdateStatementSetter(featuresToUpdate) {
		return new BatchPreparedStatementSetter() {
			def updates = featuresToUpdate
			
			int getBatchSize() {
				return updates.size();
			}
			
			void setValues(PreparedStatement ps, int i) {
				def feature = updates[i]
				int j = 1
				ps.setString(j++, feature.featureTypeId)
				ps.setString(j++, feature.featureTypeName)
				ps.setString(j++, feature.geonetworkUuid)
				ps.setString(j++, feature.gml)
				ps.setLong(j++, feature.id)
			}
		}
	}
	
	def _save(domain) {
		try {
			domain.save(failOnError: true)
		}
		catch (Exception e) {
			log.error("Failure persisting domain object ${domain}: ", e)
		}
	}

	def _getFeatureTypeRequestImplementation(metadata, indexRun, messages) {
		def featureTypeName = metadata.featureTypeName
		def featureTypeRequestClasses = FeatureTypeRequestClass.findAll("from FeatureTypeRequestClass as f where '${featureTypeName}' like f.featureTypeName || '%'")
		if (featureTypeRequestClasses) {
			def bestMatch
			featureTypeRequestClasses.each { featureTypeRequestClass ->
				if (featureTypeRequestClass.featureTypeName == featureTypeName) {
					bestMatch = featureTypeRequestClass
					// Jump out of closure and let bestMatch return this implementation
					return
				}
				else if (!bestMatch || featureTypeRequestClass.featureTypeName.length() < bestMatch.featureTypeName.length()) {
					bestMatch = featureTypeRequestClass
					log.info("Best match for feature type currently ${bestMatch.featureTypeName}")
				}
			}
			def instance = bestMatch.featureTypeRequest
			
			if (instance instanceof DiskCachingFeatureTypeRequest) {
				// Add a call back to persist features one by one for large WFS responses
				instance.featureCallback = { feature ->
					_saveFeatures(metadata, [feature])
					_updateIndexRun(indexRun, [metadata])
				}
			}
			
			return instance
		}
		messages << _getMessageForMissingFeatureTypeRequestClass(metadata)
		// Use the null implementation
		return new NullFeatureTypeRequest()
	}
	
	def _mapFeatureByFeatureTypeId(featureCollection) {
		def map = [:]
		featureCollection.each { feature ->
			map[feature.featureTypeId] = feature	
		}
		return map
	}
	
	def fetchFeatureTypes(featureTypeName, uuids, featureTypeIds) {
		def features = []
		try {
			def criteria = FeatureType.createCriteria()
			features = criteria.list {
				eq('featureTypeName', featureTypeName)
				'in'('geonetworkUuid', uuids)
				'in'('featureTypeId', featureTypeIds)
			}
		}
		catch (Exception e) {
			log.error("Failure fetching features: ${featureTypeIds}", e)
			// Rethrow ?
		}
		return features
	}
	
	def _getSliceSize() {
		def sliceSize
		try {
			sliceSize = Integer.valueOf(_getConfiguredSliceSize().toString())
		}
		catch (NumberFormatException nfe) {
			if (_getConfiguredSliceSize()) {
				log.error("Could not parse key \"feature.collection.slice.size\" value \"${s}\" to Integer")
			}
		}
		// Default to 100 when no good setting found
		if (!sliceSize || sliceSize <= 0) {
			sliceSize = 100
		}
		return sliceSize
	}
	
	def _getConfiguredSliceSize() {
		return grailsApplication.config.feature.collection.slice.size
	}
	
	def _sendMail(messages) {
		def msgBody = messages.join("\n")
		if (!messages.isEmpty()) {
			mailService.sendMail {
				to grailsApplication.config.feature.missing.email.to
				from grailsApplication.config.feature.missing.email.from
				subject grailsApplication.config.feature.missing.email.subject
				body msgBody
			}
		}
	}
	
	def _clearGorm() {
		def session = _getSession()
		session.flush()
		session.clear()
		propertyInstanceMap.get().clear()
	}
	
	def _reattach(object) {
		_getSession().lock(object, LockMode.NONE)
	}
	
	def _getSession() {
		return sessionFactory.getCurrentSession()
	}
	
	def _toFeatureServiceServers(metadataRecords) {
		def servers = [:]
		metadataRecords.each { metadata ->
			def server = servers[metadata.geoserverEndPoint]
			if (!server) {
				server = new FeatureServiceServer(metadata)
				servers[metadata.geoserverEndPoint] = server
			}
			server.addMetadata(metadata)
		}
		return servers
	}
	
	def _addFeaturesForOtherMetadata(metadata, features, metadataRecords) {
		if (!features || features.isEmpty()) {
			return
		}
		
		def feature = features[0]
		metadataRecords.each { otherMetadata ->
			if (otherMetadata.geonetworkUuid != metadata.geonetworkUuid) {
				def copy = new FeatureType(otherMetadata)
				copy.featureTypeId = feature.featureTypeId
				copy.gml = feature.gml
				features << copy
			}
		}
	}
	
	def _clearCache() {
		geoNetworkSearchSummaryCache.clear()
	}
	
	def _getMessageForMissingFeatureTypeRequestClass(metadata) {
		def ftRequest = new FeatureTypeRequest()
		def url = ftRequest.toGetUrl(metadata) + "&maxFeatures=1"
		return """\
No feature type request class configured for server ${metadata.geoserverEndPoint} feature type ${metadata.featureTypeName}.
Please add an appropriate record to table feature_type_request_class
Feature type quick link $url		
"""
	}
}

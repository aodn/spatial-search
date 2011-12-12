package au.org.emii.search.index

import org.apache.http.client.methods.HttpGet
import org.apache.http.entity.ContentProducer
import org.apache.http.entity.EntityTemplate
import org.apache.http.impl.client.BasicResponseHandler
import org.apache.http.impl.client.DefaultHttpClient
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.hibernate.LockMode;
import org.hibernate.exception.SQLGrammarException;

import au.org.emii.search.FeatureType;

class FeatureTypeRequestService {

    static transactional = true
	
	def grailsApplication
	def sessionFactory
	def mailService
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
				def future = callAsync {
					_index(indexRun, metadataList, messages)
				}
				featureCount += future.get()
			}
		}
		log.info("Finished index run $indexRun")
		_sendMail(messages)
		return featureCount
    }
	
	def _index(indexRun, metadataRecords, messages) {
		def featureCount = 0
		
		if (indexRun.documents > 0 || indexRun.failures > 0) {
			_reattach(indexRun)
		}
		def metadata = metadataRecords[0]
		def featureTypeRequestImpl = getFeatureTypeRequestImplementation(metadata, messages)
		def features = new ArrayList(featureTypeRequestImpl.requestFeatureType(metadata))
		if (!features.isEmpty()) {
			_addFeaturesForOtherMetadata(metadata, features, metadataRecords)
			_updateIndexRun(indexRun, metadataRecords)
			_saveMetadataFeatures(metadata, features)
			featureCount = features.size()
		}
		return featureCount
	}
	
	def _loadGeonetworkMetadata() {
//		def metadataRecords = GeonetworkMetadata.createCriteria() {
//			isNull("indexRun")
//			order("geoserverEndPoint")
//			order("featureTypeName")
//		}
		def metadataRecords = GeonetworkMetadata.findAllByIndexRunIsNull()
		// Piff them out of the main session transaction so when modified by the
		// async call they can be reloaded here
		metadataRecords*.discard()
	}

	def _updateIndexRun(indexRun, metadataList) {
		indexRun.geonetworkMetadataDocs.addAll(metadataList)
		indexRun.documents += metadataList.size()
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
			_saveFeatures(metadata, slice)
			_clearGorm()
			// Now evict the features from the session in the hope
			// the GC will pick them up
			//features*.discard()
			index = sliceEnd
		}
	}
	
	def _saveFeatures(metadata, features) {
		def uuids = features.collect { feature -> feature.geonetworkUuid }.unique()
		def featureTypeIds = features.collect { feature -> feature.featureTypeId }.unique()
		
		def persistedFeatures = fetchFeatureTypes(metadata.featureTypeName, uuids, featureTypeIds)
		def persistedFeaturesMap = _mapFeatureByFeatureTypeId(persistedFeatures)
		features.each() { feature ->
			def featureToPersist = persistedFeaturesMap[feature.featureTypeId]
			if (featureToPersist) {
				featureToPersist.geometry = feature.geometry
			}
			else {
				featureToPersist = feature
			}
			_save(featureToPersist)
		}
	}
	
	def _save(domain) {
		try {
			domain.save(failOnError: true)
		}
		catch (Exception e) {
			// Would love to do Haiku error messages!
			log.error("Failure persisting domain object ${domain}: ", e)
		}
	}

	def getFeatureTypeRequestImplementation(metadata, messages) {
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
			return bestMatch.featureTypeRequest
		}
		messages << "No feature type request class configured for server ${metadata.geoserverEndPoint} feature type $featureTypeName please add an appropriate record to table feature_type_request_class"
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
		// Default to 100 when no setting found
		def sliceSize = 100
		try {
			def s = grailsApplication.config.feature.collection.slice.size
			if (s) {
				sliceSize = Integer.valueOf(s)
			}
			else {
				log.warn('No configuration setting for key "feature.collection.slice.size"')
			}
		}
		catch (NumberFormatException nfe) {
			log.error("Could not parse key \"feature.collection.slice.size\" value \"${s}\" to Integer")
		}
		return sliceSize
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
				copy.geometry = feature.geometry
				features << copy
			}
		}
	}
}

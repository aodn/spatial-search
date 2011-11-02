package au.org.emii.search.index

import org.apache.http.client.methods.HttpGet
import org.apache.http.entity.ContentProducer
import org.apache.http.entity.EntityTemplate
import org.apache.http.impl.client.BasicResponseHandler
import org.apache.http.impl.client.DefaultHttpClient
import org.codehaus.groovy.grails.commons.GrailsApplication;
import org.hibernate.exception.SQLGrammarException;

import au.org.emii.search.FeatureType;

class FeatureTypeRequestService {

    static transactional = true
	
	def grailsApplication
	def featureTypeRequestFactory = [:]
	
    def index() {
		def featureCount = 0
		def indexRun = new IndexRun()
		def metadataRecords = GeonetworkMetadata.findAllByIndexRunIsNull()
		metadataRecords.each { metadata ->
			def featureTypeRequestImpl = getFeatureTypeRequestImplementation(metadata.featureTypeName)
			
			try {
				def features = new ArrayList(featureTypeRequestImpl.requestFeatureType(metadata))
				if (!features.isEmpty()) {
					saveFeatures(metadata, features)
					featureCount += features.size()
				}
			}
			catch (Exception e) {
				log.error('', e)
				indexRun.failures++
			}
			indexRun.addToGeonetworkMetadataDocs(metadata)
			
			/*
			 * This looks stupid and inefficient but if save is not called on
			 * IndexRun here an exception relating to a transient object is 
			 * thrown
			 */
			indexRun.documents++
			save(indexRun)
		}
		return featureCount
    }
	
	def saveFeatures(metadata, features) {
		def featureCount = features.size()
		def index = 0
		def sliceSize = getSliceSize()
		while (index < featureCount) {
			def sliceEnd = index + (index + sliceSize < featureCount ? sliceSize : featureCount - index)
			log.debug("Slicing from $index to " + sliceEnd)
			def slice = features.subList(index, sliceEnd)
			_saveFeatures(metadata, slice)
			index = sliceEnd
		}
	}
	
	def _saveFeatures(metadata, features) {
		def uuids = features.collect { feature -> feature.geonetworkUuid }.unique()
		def featureTypeIds = features.collect { feature -> feature.featureTypeId }.unique()
		
		def persistedFeatures = fetchFeatureTypes(metadata.featureTypeName, uuids, featureTypeIds)
		def persistedFeaturesMap = mapFeatureByFeatureTypeId(persistedFeatures)
		features.each() { feature ->
			def featureToPersist = persistedFeaturesMap[feature.featureTypeId]
			if (featureToPersist) {
				featureToPersist.geometry = feature.geometry
			}
			else {
				featureToPersist = feature
			}
			save(featureToPersist)
		}
	}
	
	def save(domain) {
		try {
			domain.save(failOnError: true)
		}
		catch (Exception e) {
			// Would love to do Haiku error messages!
			log.error("Failure persisting domain object ${domain}: ", e)
		}
	}

	def getFeatureTypeRequestImplementation(featureTypeName) {
		def impl = featureTypeRequestFactory[featureTypeName]
		if (!impl) {
			def featureTypeRequestClass = FeatureTypeRequestClass.findByFeatureTypeName(featureTypeName)
			if (featureTypeRequestClass) {
				impl = featureTypeRequestClass.featureTypeRequest
			}
			else {
				// Use the null implementation
				impl = new NullFeatureTypeRequest()
			}
			featureTypeRequestFactory[featureTypeName] = impl
		}
		return impl
	}
	
	def mapFeatureByFeatureTypeId(featureCollection) {
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
	
	def getSliceSize() {
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
}

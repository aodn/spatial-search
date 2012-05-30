package au.org.emii.search.index

import org.slf4j.Logger
import org.slf4j.LoggerFactory

// Marker for ncWMS feature types / place holder for future ncWMS feature type request processing 

class NcwmsFeatureTypeRequest extends FeatureTypeRequest {

	static final Logger log = LoggerFactory.getLogger(NcwmsFeatureTypeRequest.class)
	
	def handleResponse(metadata, xml) {
		log.info("No response expected when using NcwmsFeatureTypeRequest for " + metadata.featureTypeName)
		return [] as Set
	}

	def requestFeatureType(metadata) {
		log.info("No request sent when using NcwmsFeatureTypeRequest for " + metadata.featureTypeName)
		return handleResponse(metadata, null)
	}
}

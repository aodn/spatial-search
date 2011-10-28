package au.org.emii.search.index

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class NullFeatureTypeRequest extends FeatureTypeRequest {

	static final Logger log = LoggerFactory.getLogger(NullFeatureTypeRequest.class)
	
	def handleResponse(queuedDocument, xml) {
		log.info("No response expected when using NullFeatureTypeRequest for " + queuedDocument.featureTypeName)
		return [] as Set
	}

	def requestFeatureType(queuedDocument) {
		log.info("No request sent when using NullFeatureTypeRequest for " + queuedDocument.featureTypeName)
		return handleResponse(queuedDocument, null)
	}
}

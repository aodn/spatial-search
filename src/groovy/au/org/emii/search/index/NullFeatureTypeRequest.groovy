
/*
 * Copyright 2012 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */
package au.org.emii.search.index

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class NullFeatureTypeRequest extends FeatureTypeRequest {

	static final Logger log = LoggerFactory.getLogger(NullFeatureTypeRequest.class)
	
	def handleResponse(metadata, xml) {
		log.info("No response expected when using NullFeatureTypeRequest for " + metadata.featureTypeName)
		return [] as Set
	}

	def requestFeatureType(metadata) {
		log.info("No request sent when using NullFeatureTypeRequest for " + metadata.featureTypeName)
		return handleResponse(metadata, null)
	}
}

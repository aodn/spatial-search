
/*
 * Copyright 2012 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */
package au.org.emii.search.index

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import au.org.emii.search.FeatureType;

// Marker for ncWMS feature types / place holder for future ncWMS feature type request processing 

class NcwmsFeatureTypeRequest extends FeatureTypeRequest {

    static final Logger log = LoggerFactory.getLogger(NcwmsFeatureTypeRequest.class)

    def handleResponse(metadata, xml) {
        def feature = new FeatureType(metadata)
        feature.featureTypeId = metadata.featureTypeName
        feature.geometry = metadata.geoBox
        return [feature] as Set
    }

    def requestFeatureType(metadata) {
        log.info("No request sent when using NcwmsFeatureTypeRequest for " + metadata.featureTypeName)
        return handleResponse(metadata, null)
    }
}

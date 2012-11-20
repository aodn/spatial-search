
/*
 * Copyright 2012 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */
package au.org.emii.search.index

import au.org.emii.search.FeatureType;

class FeatureServiceServer {

	def url
	def featureTypeUuids
	
	FeatureServiceServer(geonetworkMetadata) {
		featureTypeUuids = [:]
		url = geonetworkMetadata.geoserverEndPoint
	}
	
	def addMetadata(geonetworkMetadata) {
		def metadataList = featureTypeUuids[geonetworkMetadata.featureTypeName]
		if (!metadataList) {
			metadataList = []
			featureTypeUuids[geonetworkMetadata.featureTypeName] = metadataList
		}
		metadataList << geonetworkMetadata
	}
}

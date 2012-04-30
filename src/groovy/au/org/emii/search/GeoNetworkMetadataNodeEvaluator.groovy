package au.org.emii.search

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class GeoNetworkMetadataNodeEvaluator {
	
	static final Logger log = LoggerFactory.getLogger(GeoNetworkMetadataNodeEvaluator.class)
	
	def grailsApplication
	def metadataNode
	def protocol
	def nsGeonet = new GeoNetworkNamespace()

	GeoNetworkMetadataNodeEvaluator(grailsApplication, metadataNode, protocol) {
		this.grailsApplication = grailsApplication
		this.metadataNode = metadataNode
		this.protocol = protocol
	}
	
	def includeInResponse(metadataNode, featureUuids) {
		if (protocol) {
			return _isNodeWithSpatialFeature(metadataNode, featureUuids)
		}
		
		return _isNodeWithSpatialFeature(metadataNode, featureUuids) || _isNotMapLayer(metadataNode)
	}
	
	def _isNotMapLayer(metadataNode) {
		def result = true
		metadataNode.link.each { link ->
			if (link) {
				def gnLink = new GeoNetworkLink(grailsApplication, link.text())
				if (gnLink.isMapLink()) {
					result = false
				}
			}
		}
		return result
	}
	
	def _isNodeWithSpatialFeature(metadataNode, featureUuids) {
		return featureUuids.contains(nsGeonet.parseUuid(metadataNode))
	}
}

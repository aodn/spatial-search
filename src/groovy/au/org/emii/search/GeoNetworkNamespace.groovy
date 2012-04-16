package au.org.emii.search

import groovy.xml.Namespace

class GeoNetworkNamespace extends Namespace {

	def ns
	
	GeoNetworkNamespace() {
		ns = new Namespace('http://www.fao.org/geonetwork', 'geonet')
	}
	
	def parseUuid(metadataNode) {
		return metadataNode[ns.info][0].uuid.text()
	}
}

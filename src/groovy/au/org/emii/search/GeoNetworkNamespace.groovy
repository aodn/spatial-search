package au.org.emii.search

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import groovy.xml.Namespace

class GeoNetworkNamespace extends Namespace {

	def ns
	def changeDateFormat
	
	GeoNetworkNamespace() {
		ns = new Namespace('http://www.fao.org/geonetwork', 'geonet')
		changeDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
	}
	
	def parseUuid(metadataNode) {
		return metadataNode[ns.info][0].uuid.text()
	}
	
	def parseChangeDate(metadataNode) {
		def dateText = metadataNode[ns.info][0].changeDate.text()
		return new Timestamp(changeDateFormat.parse(dateText).time)
	}
}

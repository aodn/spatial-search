
/*
 * Copyright 2012 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */
package au.org.emii.search


import org.slf4j.Logger
import org.slf4j.LoggerFactory

import au.org.emii.search.geometry.GeometryHelper
import au.org.emii.search.geometry.GeometryHelper.CoordinateFormat
import au.org.emii.search.index.GeonetworkMetadata

class GeoNetworkResponse {

	static Logger log = LoggerFactory.getLogger(GeoNetworkResponse.class)

	def tree
	def uuids
	def keywordCounts
	def metadataList
	def grailsApplication
	def from
	def to
	def count
	def nsGeonet

	GeoNetworkResponse(grailsApplication) {
		this.grailsApplication = grailsApplication
	}

	GeoNetworkResponse(grailsApplication, xml) {
		this(grailsApplication)

		if (!xml) {
			throw new NullPointerException('Cannot build response, geonetwork XML should not be null')
		}

		uuids = []
		keywordCounts = [:]
		metadataList = []

		def validating = false
		def namespaceAware = true

		nsGeonet = new GeoNetworkNamespace()
		tree = new XmlParser(validating, namespaceAware).parseText(xml)

		from = tree.@from.toInteger()
		to = tree.@to.toInteger()
		// Note this value is currently not updated against this response when
		// the count is updated in the XML to be returned
		count = tree.summary[0].@count.toInteger()
	}

	def getGeonetworkMetadataObjects() {
		return _parseXmlToGeonetworkMetadataObjects() { metadataNode ->
			_collectUuid(metadataNode)
		}
	}

	def getUuids() {
		tree.metadata.each { metadataNode ->
			_collectUuid(metadataNode)
		}
		return uuids
	}

	def _parseXmlToGeonetworkMetadataObjects(metadataElementClosure) {
		def allRecords = tree.metadata
		log.debug("${allRecords.size()} metadata records returned")

		return _parseMetadataElements(metadataElementClosure)
	}

	def _parseMetadataElements(metadataElementClosure) {
		metadataList = [] as Set

		tree.metadata.each { metadataNode ->
			_parseLinkElements(metadataNode)
			if (metadataElementClosure) {
				metadataElementClosure(metadataNode)
			}
		}
		return metadataList
	}

	def _parseUuid(metadataNode) {
		return nsGeonet.parseUuid(metadataNode)
	}

	def _parseChangeDate(metadataNode) {
		return nsGeonet.parseChangeDate(metadataNode)
	}

	def _parseLinkElements(metadataNode) {
		metadataNode.link.each { link ->
			def metadata = _parseLinkElement(link)
			if (metadata) {
				metadata.geonetworkUuid = _parseUuid(metadataNode)
				metadata.changeDate = _parseChangeDate(metadataNode)
				metadata.geoBox = _parseGeoBox(metadataNode)
				metadataList << metadata
				log.debug(metadata.toString())
			}
		}
	}

	def _parseLinkElement(link) {
		def metadata
		if (link) {
			def gnLink = new GeoNetworkLink(grailsApplication, link.text())
			if (gnLink.isMapLink()) {
				metadata = new GeonetworkMetadata()
				metadata.featureTypeName = gnLink.featureType
				metadata.geoserverEndPoint = _serverEndPointFrom(gnLink.href)
			}
		}
		return metadata
	}

	def _serverEndPointFrom(url) {
		def markPoint = url.indexOf("?")
		def endPoint = markPoint > 0 ? url.substring(0, markPoint) : url
		return endPoint.substring(0, endPoint.lastIndexOf("/"))
	}

	def _collectUuid(metadataNode) {
		uuids << _parseUuid(metadataNode)
	}

	def _parseGeoBox(metadataNode) {
		if (metadataNode.geoBox.isEmpty()) {
			return
		}

        def helper = new GeometryHelper()
		def geoBoxes = _toBoundingBoxStringArrays(metadataNode)
        if (geoBoxes.size() > 1) {
            return _parseGeoBoxes(geoBoxes, helper)
		}

        return helper.toBoundingBox(geoBoxes[0][0].split(" "))
	}

    def _parseGeoBoxes(geoBoxes, geometryHelper) {
        if (geometryHelper.isMultiPoint(geoBoxes)) {
            return geometryHelper.toGeometryFromCoordinateText('MultiPoint', geoBoxes)
        }

        return geometryHelper.toMultiPolygonFromGeoNetworkGeoBoxes(geoBoxes)
    }

    def _toBoundingBoxStringArrays(metadataNode) {
        def geoBoxes = []
        metadataNode.geoBox.each {
            geoBoxes << [it.text().replaceAll("\\|", " ")]
        }

        return geoBoxes
    }
}

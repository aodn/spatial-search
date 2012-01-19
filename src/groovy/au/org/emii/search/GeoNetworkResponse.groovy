package au.org.emii.search

import groovy.xml.Namespace;
import groovy.xml.StreamingMarkupBuilder

import org.apache.commons.lang.builder.CompareToBuilder;
import org.slf4j.Logger
import org.slf4j.LoggerFactory

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
	
	GeoNetworkResponse(grailsApplication, xml) {
		if (!xml) {
			throw new NullPointerException('Cannot build geonetwork XML should not be null')
		}
		
		uuids = []
		keywordCounts = [:]
		metadataList = []
		this.grailsApplication = grailsApplication
		
		def validating = false
		def namespaceAware = true
		// TODO extract namespace to config?
		nsGeonet = new Namespace('http://www.fao.org/geonetwork', 'geonet')
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
	
	def getSpatialResponse(metadataCollection, features, pageSize) {
		if (!features || features.isEmpty()) {
			return _emptyResponse()
		}
		
		def featureUuids = features.collect { feature -> feature.geonetworkUuid }
		_strip(featureUuids, pageSize)
		_updateKeywordCounts()
		return _buildResponseXml()
	}
	
	def _parseXmlToGeonetworkMetadataObjects(metadataElementClosure) {
		def allRecords = tree.metadata
		log.debug("${allRecords.size()} metadata records returned")
		
		return _parseMetadataElements(metadataElementClosure)
	}
	
	def _parseMetadataElements(metadataElementClosure) {
		metadataList = [] as Set
		
		tree.metadata.each { metadataNode ->
			def uuid = _parseUuid(metadataNode)
			def metadata = _parseLinkElements(metadataNode, uuid)
			if (metadataElementClosure) {
				metadataElementClosure(metadataNode)
			}
		}
		return metadataList
	}
	
	def _parseUuid(metadataNode) {
		return metadataNode[nsGeonet.info][0].uuid.text()
	}
	
	def _parseLinkElements(metadataNode, uuid) {
		def metadata
		metadataNode.link.each { link ->
			metadata = _parseLinkElement(link)
			if (metadata) {
				metadata.geonetworkUuid = uuid
				metadataList << metadata
			}
		}
		return metadata
	}
	
	def _parseLinkElement(link) {
		def metadata
		if (_isProtocol(link.@protocol) && _isFeatureTypeLink(link.@name)) {
			metadata = new GeonetworkMetadata()
			metadata.featureTypeName = link.@name
			metadata.geoserverEndPoint = _serverEndPointFrom(link.@href)
		}
		return metadata
	}
	
	def _isProtocol(text) {
		def protocol = grailsApplication.config.geonetwork.link.protocol.regex
		// The true is here to coerce the matcher into a boolean
		return true && text =~ /$protocol/
	}
	
	def _isFeatureTypeLink(text) {
		def regexPattern = grailsApplication.config.geonetwork.feature.type.indentifier.regex
		// The true is here to coerce the matcher into a boolean
		return true && text =~ /$regexPattern/
	}
	
	def _serverEndPointFrom(url) {
		def markPoint = url.indexOf("?")
		def endPoint = markPoint > 0 ? url.substring(0, markPoint) : url
		return endPoint.substring(0, endPoint.lastIndexOf("/"))
	}
	
	def _collectUuid(metadataNode) {
		uuids << _parseUuid(metadataNode)
	}
	
	def _buildResponseXml() {
		def writer = new StringWriter()
		
		def indentPrinter = new IndentPrinter(writer, '', false)
		indentPrinter.indentLevel = 0
		new XmlNodePrinter(indentPrinter).print(tree)
		return writer.toString()
	}
	
	def _strip(featureUuids, pageSize) {
		// If no page size has been specified give it a huge value to make the
		// flow control boolean work and be easy to understand
		if (!pageSize) {
			pageSize = Integer.MAX_VALUE
		}
		def i = 1
		tree.metadata.each { metadataNode ->
			def uuid = _parseUuid(metadataNode)
			if (i > pageSize || !featureUuids.contains(uuid)) {
				_decrementSummaryCounts()
				metadataNode.replaceNode {}
			}
			else {
				_incrementKeywordCounts(metadataNode)
				i++
			}
		}
	}
	
	def _decrementSummaryCounts() {
		_updateSummaryCount(tree.summary[0].@count.toInteger() - 1)
		_updateHitsUsedForSummaryCount(tree.summary[0].@hitsusedforsummary.toInteger() - 1)
	}
	
	def _updateSummaryCount(count) {
		tree.summary[0].@count = count.toString()
	}
	
	def _updateHitsUsedForSummaryCount(count) {
		tree.summary[0].@hitsusedforsummary = count.toString()
	}
	
	def _updateKeywordCounts() {
		def sorted = keywordCounts.entrySet().sort{ it.value }.reverse()
		if (sorted.size() > 10) {
			sorted = sorted[0..9]
		}
		
		tree.summary.keywords[0].children().clear()
		sorted.each	 { entry ->
			tree.summary.keywords[0].appendNode("keyword", [count: entry.value, name: entry.key])
		}
	}
	
	def _incrementKeywordCounts(metadataNode) {
		metadataNode.keyword.each { keyword ->
			def lCount = keywordCounts[keyword.text()]
			if (!lCount) {
				lCount = 0
			}
			keywordCounts[keyword.text()] = lCount + 1
		}
	}
	
	def _emptyResponse() {
		def r = """<response from="0" to="0" selected="0">
		<summary count="0" type="local" hitsusedforsummary="0">
			<dataParameters/>
			<keywords/>
			<organizationNames/>
		</summary>
</response>"""
	}
}

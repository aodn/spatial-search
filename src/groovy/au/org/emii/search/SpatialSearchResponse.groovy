package au.org.emii.search

import groovy.xml.MarkupBuilder;

import java.io.StringWriter;
import java.util.concurrent.Callable;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

class SpatialSearchResponse {
	
	static Logger log = LoggerFactory.getLogger(SpatialSearchResponse.class)
	
	def grailsApplication
	def numberOfResultsToReturn = Long.MAX_VALUE
	def metadataTree
	def nsGeonet
	def summaryNode
	def metadataNodes
	def to
	def future
	def params
	def count
	def keywordSummary
	def executorService
	def geoNetworkSearchSummaryService
	
	SpatialSearchResponse(grailsApplication, params, numberOfResultsToReturn, executorService, geoNetworkSearchSummaryService) {
		this.params = params
		this.grailsApplication = grailsApplication
		this.executorService = executorService
		this.geoNetworkSearchSummaryService = geoNetworkSearchSummaryService
		metadataNodes = []
		nsGeonet = new GeoNetworkNamespace()
		keywordSummary = new GeoNetworkKeywordSummary()
		if (numberOfResultsToReturn) {
			this.numberOfResultsToReturn = numberOfResultsToReturn
		}
	}
	
	def addResponse(features, geoNetworkResponse) {
		_initMetadataTree(geoNetworkResponse.tree)
		to = geoNetworkResponse.tree.@to.toInteger()
		
		if (_morePagesAreAvailable()) {
			_getKeywordSummary(geoNetworkResponse.tree.summary[0].@count.toInteger()) 
		}
		
		if (CollectionUtils.isNotEmpty(features)) {
			_collect(features, geoNetworkResponse.tree)
		}
		
		return _isNotComplete()
	}
	
	def getResponse() {
		if (isEmpty()) {
			return _emptyResponse()
		}
		return _spatialResponse()
	}
	
	def _spatialResponse() {
		if (future) {
			def futuredSummary = future.get()
			if (futuredSummary) {
				keywordSummary.merge(futuredSummary)
			}
		}
		
		def writer = new StringWriter()
		def builder = new MarkupBuilder(writer)
		builder.response(from: metadataTree.@from, to: keywordSummary.to, selected: metadataTree.@selected) {
			_buildSummaryNode(builder)
			mkp.yieldUnescaped(_printMetadataNodes())
		}
		return writer.toString()
	}
	
	def _buildSummaryNode(builder) {
		def keywordSummariesToDisplay = keywordSummary.getKeywords()
		if (keywordSummariesToDisplay?.size() > 10) {
			keywordSummariesToDisplay = keywordSummariesToDisplay[0..9]
		}
		builder.summary(count: keywordSummary.hitsUsedForSummary, type: summaryNode.@type, hitsusedforsummary: keywordSummary.hitsUsedForSummary) {
			keywordSummariesToDisplay.each { keyword ->
				_buildSummaryKeywordNode(builder, keyword)
			}
		}
	}
	
	def _buildSummaryKeywordNode(builder, keyword) {
		builder.keyword(count: keyword.count, name: keyword.name, indexKey: keyword.indexKey)
	}
	
	def _printMetadataNodes() {
		def writer = new StringWriter()
		def nodePrinter = new XmlNodePrinter(new PrintWriter(writer))
		metadataNodes.each { node ->
			nodePrinter.print(node)
		}
		return writer.toString()
	}
	
	def _initMetadataTree(responseMetadataTree) {
		if (!this.metadataTree) {
			this.metadataTree = responseMetadataTree
			_setSummaryNode(metadataTree.summary[0])
		}
	}
	
	def _setSummaryNode(summaryNode) {
		this.summaryNode = summaryNode
		this.count = summaryNode.@count.toInteger()
	}
	
	def _collect(featureUuids, responseMetadataTree) {
		responseMetadataTree.metadata.each { metadataNode ->
			if (_isNotComplete() && _isNodeWithSpatialFeature(metadataNode, featureUuids)) {
				_addMetadataNode(metadataNode)
			}
		}
	}
	
	def _isNodeWithSpatialFeature(metadataNode, featureUuids) {
		return featureUuids.contains(nsGeonet.parseUuid(metadataNode))
	}
	
	def _isNotComplete() {
		metadataNodes.size() < numberOfResultsToReturn
	}
	
	def _addMetadataNode(metadataNode) {
		keywordSummary.addNodeKeywords(metadataNode)
		metadataNodes << metadataNode
	}
	
	def _morePagesAreAvailable() {
		def pageSize = grailsApplication.config.geonetwork.search.page.size.toInteger()
		return summaryNode.@count.toInteger() > to + pageSize
	}
	
	def _getKeywordSummary(count) {
		future = executorService.submit({
			geoNetworkSearchSummaryService.calculateSummaryKeywords(count, params)
		} as Callable)
	}
	
	def isEmpty() {
		return metadataNodes.isEmpty()
	}
	
	def _emptyResponse() {
		return """<response from="0" to="0" selected="0">
		<summary count="0" type="local" hitsusedforsummary="0">
			<dataParameters/>
			<keywords/>
			<organizationNames/>
		</summary>
</response>"""
	}
}

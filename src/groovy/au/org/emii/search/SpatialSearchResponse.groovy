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
	def metadataNodes
	def future
	def params
	def count
	def executorService
	def geoNetworkSearchSummaryService
	def geoNetworkSearchSummaryCache
	def geoNetworkSearchShaBuilder
	
	def setup(params, numberOfResultsToReturn, executorService) {
		this.params = new HashMap(params)
		this.executorService = executorService
		metadataNodes = []
		nsGeonet = new GeoNetworkNamespace()
		if (numberOfResultsToReturn) {
			this.numberOfResultsToReturn = numberOfResultsToReturn
		}
	}
	
	def addResponse(features, geoNetworkResponse) {
		_initMetadataTree(geoNetworkResponse.tree)
		_requestKeywordSummary(geoNetworkResponse.tree.summary[0].@count.toInteger()) 
		
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
	
	def updatePageParams(params) {
		def summary = _getCachedKeywordSummary()
		if (summary) {
			summary.updatePageParams(params)
			return true
		}
		return false
	}
	
	def _spatialResponse() {
		def summary = _getKeywordSummary()
		
		def writer = new StringWriter()
		def builder = new MarkupBuilder(writer)
		
		builder.response(from: params.from, to: params.to, selected: metadataTree.@selected) {
			summary.buildSummaryXmlNode(builder)
			mkp.yieldUnescaped(_printMetadataNodes())
		}
		return writer.toString()
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
		}
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
		metadataNodes << metadataNode
	}
	
	def _requestKeywordSummary(count) {
		if (!_getCachedKeywordSummary()) {
			future = executorService.submit({
				geoNetworkSearchSummaryService.calculateSummaryKeywords(count, params)
			} as Callable)
		}
	}
	
	def _getKeywordSummary() {
		def summary = _getCachedKeywordSummary() 
		if (!summary) {
			summary = _getFuture()
			_cacheSummary(summary)
		}
		return summary
	}
	
	def _getCachedKeywordSummary() {
		return geoNetworkSearchSummaryCache.get(_getSha())
	}
	
	def _getFuture() {
		return future.get()
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
	
	def _getSha() {
		return geoNetworkSearchShaBuilder.buildSha(params)
	}
	
	def _cacheSummary(keywordSummary) {
		geoNetworkSearchSummaryCache.add(_getSha(), keywordSummary)
	}
}

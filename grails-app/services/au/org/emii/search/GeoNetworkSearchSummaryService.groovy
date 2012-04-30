package au.org.emii.search

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory

class GeoNetworkSearchSummaryService extends GeoNetworkRequestService {
	
	static final Logger log = LoggerFactory.getLogger(GeoNetworkSearchSummaryService.class)

    static transactional = true
	
	def cutOff = 5

    def calculateSummaryKeywords(count, params) {
		def paramsCopy = new HashMap(params)
		def searchRequestPageSize = _calculateSearchRequestPageSize(paramsCopy)
		
		// Start at the first page
		_updateNumericParam(paramsCopy, 'from', 1)
		def pageSize = _calcuatePageSize(count, paramsCopy.from.toInteger())
		_updateNumericParam(paramsCopy, 'to', pageSize)
		
		def firstTime = true
		def keywordSummary = new GeoNetworkKeywordSummary()
		while (_getNumericParam(paramsCopy, 'to') < count || firstTime) {
			firstTime = false
			_fetchPage(paramsCopy, keywordSummary, searchRequestPageSize)
			_updateNumericParam(paramsCopy, 'from', _getNumericParam(paramsCopy, 'to') + 1)
			_updateNumericParam(paramsCopy, 'to', _getNumericParam(paramsCopy, 'to') + pageSize)
		}
		keywordSummary.close(searchRequestPageSize)
		return keywordSummary
    }
	
	def _fetchPage(params, keywordSummary, searchRequestPageSize) {
		def xml = _geoNetworkSearch(params)
		def geoNetworkResponse = new GeoNetworkResponse(grailsApplication, xml)
		def features = _searchForFeatures(params, geoNetworkResponse.getUuids())
		
		if (_canFastForward(params, features)) {
			_fastForward(geoNetworkResponse, keywordSummary)
		}
		else {
			_parseMetadataNodes(params, keywordSummary, searchRequestPageSize, geoNetworkResponse, features)
		}
	}
	
	def _calcuatePageSize(count, from) {
		def result = grailsApplication.config.geonetwork.search.page.size.toInteger()
		if (_hasMorePagesThanCutOff(count, from, result)) {
			result = (count - from) / cutOff
		}
		
		return result
	}
	
	def _hasMorePagesThanCutOff(count, from, pageSize) {
		return (count - from) / pageSize > cutOff
	}
	
	def _calculateSearchRequestPageSize(params) {
		return _getNumericParam(params, 'to') - _getNumericParam(params, 'from') + 1
	}
	
	def _canFastForward(params, features) {
		return params.protocol && CollectionUtils.isEmpty(features)
	}
	
	def _fastForward(geoNetworkResponse, keywordSummary) {
		log.debug("Fast forward")
		keywordSummary.pageFastForward(geoNetworkResponse.tree.metadata.size())
	}
	
	def _parseMetadataNodes(params, keywordSummary, searchRequestPageSize, geoNetworkResponse, features) {
		geoNetworkResponse.tree.metadata.each { metadataNode ->
			if (_includeInResponse(params, metadataNode, features)) {
				log.debug("Including metadata $metadataNode.title")
				keywordSummary.addNodeKeywords(metadataNode)
			}
			keywordSummary.page(searchRequestPageSize)
		}
	}
	
	def _includeInResponse(params, metadataNode, features) {
		def evaluator = new GeoNetworkMetadataNodeEvaluator(grailsApplication, metadataNode, params.protocol)
		return evaluator.includeInResponse(metadataNode, features)
	}
}

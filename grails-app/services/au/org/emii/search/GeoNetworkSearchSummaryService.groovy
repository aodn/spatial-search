package au.org.emii.search

import org.apache.commons.collections.CollectionUtils;

class GeoNetworkSearchSummaryService extends GeoNetworkRequestService {

    static transactional = true
	
	def cutOff = 5
	def nsGeonet = new GeoNetworkNamespace()

    def calculateSummaryKeywords(count, params) {
		def paramsCopy = new HashMap(params)
		def searchRequestPageSize = _calculateSearchRequestPageSize(paramsCopy)
		
		// Start at the first page
		_updateNumericParam(paramsCopy, 'from', 1)
		_updateNumericParam(paramsCopy, 'to', searchRequestPageSize + 1)
		
		def pageSize = _calcuatePageSize(count, paramsCopy.from.toInteger())
		def keywordSummary = new GeoNetworkKeywordSummary()
		while (_getNumericParam(paramsCopy, 'to') < count) {
			_fetchPage(paramsCopy, keywordSummary, searchRequestPageSize)
			_updateNumericParam(paramsCopy, 'from', _getNumericParam(paramsCopy, 'to') + 1)
			_updateNumericParam(paramsCopy, 'to', _getNumericParam(paramsCopy, 'to') + pageSize)
		}
		keywordSummary.close()
		return keywordSummary
    }
	
	def _fetchPage(params, keywordSummary, searchRequestPageSize) {
		def xml = _geoNetworkSearch(params)
		def geoNetworkResponse = new GeoNetworkResponse(grailsApplication, xml)
		def features = _searchForFeatures(params, geoNetworkResponse.getUuids())
		
		if (CollectionUtils.isNotEmpty(features)) {
			geoNetworkResponse.tree.metadata.each { metadataNode ->
				if (features.contains(nsGeonet.parseUuid(metadataNode))) {
					keywordSummary.addNodeKeywords(metadataNode)
				}
				keywordSummary.page(searchRequestPageSize)
			}
		}
		else {
			keywordSummary.pageFastForward(geoNetworkResponse.tree.metadata.size())
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
}

package au.org.emii.search

import org.apache.commons.collections.CollectionUtils;

class GeoNetworkSearchSummaryService extends GeoNetworkRequestService {

    static transactional = true
	
	def cutOff = 5
	def nsGeonet = new GeoNetworkNamespace()

    def calculateSummaryKeywords(count, params) {
		def pageSize = _calcuatePageSize(count, params.from.toInteger())
		def keywordSummary = new GeoNetworkKeywordSummary()
		while (_getNumericParam(params, 'to') < count) {
			_updateNumericParam(params, 'from', _getNumericParam(params, 'to') + 1)
			_updateNumericParam(params, 'to', _getNumericParam(params, 'to') + pageSize)
			_fetchPage(params, keywordSummary)
		}
		return keywordSummary
    }
	
	def _fetchPage(params, keywordSummary) {
		def xml = _geoNetworkSearch(params)
		def geoNetworkResponse = new GeoNetworkResponse(grailsApplication, xml)
		keywordSummary.to = geoNetworkResponse.tree.@to.toInteger()
		def features = _searchForFeatures(params, geoNetworkResponse.getUuids())
		
		if (CollectionUtils.isNotEmpty(features)) {
			geoNetworkResponse.tree.metadata.each { metadataNode ->
				if (features.contains(nsGeonet.parseUuid(metadataNode))) {
					keywordSummary.addNodeKeywords(metadataNode)
				}
			}
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
}

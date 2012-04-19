// Place your Spring DSL code here
beans = {
	geoNetworkRequest(au.org.emii.search.GeoNetworkRequest) {
		grailsApplication = ref('grailsApplication')
	}
	
	geoNetworkSearchSummaryCache(au.org.emii.search.GeoNetworkSearchSummaryCache)
	
	geoNetworkSearchShaBuilder(au.org.emii.search.GeoNetworkSearchShaBuilder)
	
	spatialSearchResponse(au.org.emii.search.SpatialSearchResponse) { bean ->
		bean.scope = 'request'
		grailsApplication = ref('grailsApplication')
		geoNetworkSearchSummaryService = ref('geoNetworkSearchSummaryService')
		geoNetworkSearchSummaryCache = ref('geoNetworkSearchSummaryCache')
		geoNetworkSearchShaBuilder = ref('geoNetworkSearchShaBuilder')
	}
}

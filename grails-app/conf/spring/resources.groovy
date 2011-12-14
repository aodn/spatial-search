// Place your Spring DSL code here
beans = {
	geoNetworkRequest(au.org.emii.search.GeoNetworkRequest) {
		grailsApplication = ref('grailsApplication')
	}
}

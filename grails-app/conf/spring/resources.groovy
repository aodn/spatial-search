// Place your Spring DSL code here
beans = {
	geoNetworkRequest(au.org.emii.search.GeoNetworkRequest) {
		grailsApplication = ref('grailsApplication')
	}
	
	dataSource(org.apache.commons.dbcp.BasicDataSource) {
		minEvictableIdleTimeMillis=1800000
		timeBetweenEvictionRunsMillis=1800000
		numTestsPerEvictionRun=3
		testOnBorrow=true
		testWhileIdle=true
		testOnReturn=true
		validationQuery="SELECT 1"
	}
}

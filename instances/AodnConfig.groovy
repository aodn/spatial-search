// environment specific settings
environments {
	
	development {
		grails.serverURL = "http://localhost:8080"
		geonetwork.search.serverURL = "http://catalogue.aodn.org.au/geonetwork/srv/eng/q"
		geonetwork.index.serverURL = "http://catalogue.aodn.org.au/geonetwork/srv/eng/q"
		geoserver.response.cache.dir = "/tmp"
		feature.missing.email.from='aodnsearch@emii.org.au'
		feature.missing.email.subject='AODN Search Index Action Required'
	}
	
	production {
		grails.serverURL = "http://aodnsearch.emii.org.au"
		geonetwork.search.serverURL = "http://catalogue.aodn.org.au/geonetwork/srv/eng/q"
		geonetwork.index.serverURL = "http://catalogue.aodn.org.au/geonetwork/srv/eng/q"
		geoserver.response.cache.dir = "/tmp"
		feature.missing.email.from='aodnsearch@emii.org.au'
		feature.missing.email.subject='AODN Search Index Action Required'
	}
}

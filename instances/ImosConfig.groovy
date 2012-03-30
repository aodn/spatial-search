// environment specific settings
environments {
	
	development {
		grails.serverURL = "http://localhost:8080"
		geonetwork.search.serverURL = "http://mest.imos.org.au/geonetwork/srv/en/imos.xml.search"
		geonetwork.index.serverURL = "http://mest.imos.org.au/geonetwork/srv/en/xml.search"
		geoserver.response.cache.dir = "/tmp"
		feature.missing.email.from='imossearch@emii.org.au'
		feature.missing.email.subject='IMOS Search Index Action Required'
	}
	
	production {
		grails.serverURL = "http://imossearch.emii.org.au"
		geonetwork.search.serverURL = "http://mest.imos.org.au/geonetwork/srv/en/imos.xml.search"
		geonetwork.index.serverURL = "http://mest.imos.org.au/geonetwork/srv/en/xml.search"
		geoserver.response.cache.dir = "/tmp"
		feature.missing.email.from='imossearch@emii.org.au'
		feature.missing.email.subject='IMOS Search Index Action Required'
	}
}


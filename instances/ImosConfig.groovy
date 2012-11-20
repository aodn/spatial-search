
/*
 * Copyright 2012 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */
// environment specific settings
environments {
	
	development {
		grails.serverURL = "http://localhost:8080"
		geonetwork.search.serverURL = "http://mest.imos.org.au/geonetwork/srv/eng/q"
		geonetwork.index.serverURL = "http://mest.imos.org.au/geonetwork/srv/eng/q"
		geoserver.response.cache.dir = "/tmp"
		feature.missing.email.from='imossearch@emii.org.au'
		feature.missing.email.subject='IMOS Search Index Action Required'
	}
	
	production {
		grails.serverURL = "http://imossearch.emii.org.au"
		geonetwork.search.serverURL = "http://mest.imos.org.au/geonetwork/srv/eng/q"
		geonetwork.index.serverURL = "http://mest.imos.org.au/geonetwork/srv/eng/q"
		geoserver.response.cache.dir = "/tmp"
		feature.missing.email.from='imossearch@emii.org.au'
		feature.missing.email.subject='IMOS Search Index Action Required'
	}
}



/*
 * Copyright 2012 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */
import org.apache.log4j.rolling.RollingFileAppender
import org.apache.log4j.rolling.TimeBasedRollingPolicy

import javax.naming.InitialContext

// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if(System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [ html: ['text/html','application/xhtml+xml'],
                      xml: ['text/xml', 'application/xml'],
                      text: 'text/plain',
                      js: 'text/javascript',
                      rss: 'application/rss+xml',
                      atom: 'application/atom+xml',
                      css: 'text/css',
                      csv: 'text/csv',
                      all: '*/*',
                      json: ['application/json','text/json'],
                      form: 'application/x-www-form-urlencoded',
                      multipartForm: 'multipart/form-data'
                    ]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// whether to install the java.util.logging bridge for sl4j. Disable for AppEngine!
grails.logging.jul.usebridge = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// Database migration.
grails.plugin.databasemigration.updateOnStart = true
grails.plugin.databasemigration.updateOnStartFileNames = ['changelog.groovy']

// set per-environment serverURL stem for creating absolute links
environments {
    production {
	    grails.serverURL = "http://search.aodn.org.au"
	    geonetwork.search.serverURL = "http://catalogue.aodn.org.au/geonetwork/srv/eng/q"
	    geonetwork.index.serverURL = "http://catalogue.aodn.org.au/geonetwork/srv/eng/q"
	    geoserver.response.cache.dir = "/tmp"
	    feature.missing.email.to='info@example.com'
	    feature.missing.email.from='aodnsearch@emii.org.au'
	    feature.missing.email.subject='AODN Search Index Action Required'
    }
    development {
        grails.serverURL = "http://localhost:${grails.server.port.http}/${appName}"
		geonetwork.search.serverURL = "http://catalogue.aodn.org.au/geonetwork/srv/eng/q"
		geonetwork.index.serverURL = "http://catalogue.aodn.org.au/geonetwork/srv/eng/q"
		geoserver.response.cache.dir = "/tmp"
		feature.missing.email.to='info@example.com'
		feature.missing.email.from='spatialsearch@emii.org.au'
		feature.missing.email.subject='Spatial Search Index Action Required'
    }
    test {
        grails.serverURL = "http://localhost:${grails.server.port.http}/${appName}"
		geonetwork.search.serverURL = "http://mest-test.emii.org.au/geonetwork/srv/eng/q"
		geonetwork.index.serverURL = "http://mest-test.emii.org.au/geonetwork/srv/eng/q"
		geoserver.response.cache.dir = "/tmp"
		feature.missing.email.to='info@example.com'
		feature.missing.email.from='spatialsearch@emii.org.au'
		feature.missing.email.subject='Spatial Search Index Action Required'
    }

}

// log4j configuration
log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}

	def rollingFile = new RollingFileAppender(name: 'file', layout: pattern(conversionPattern: '%d{ABSOLUTE}:%p:%c: %m%n'))
	def rollingPolicy = new TimeBasedRollingPolicy(fileNamePattern: '/var/log/spatialsearch/spatialsearch.%d{yyyy-ww}.gz', activeFileName: '/var/log/spatialsearch/spatialsearch.log')
	
	rollingPolicy.activateOptions()
	rollingFile.setRollingPolicy(rollingPolicy)
	
	appenders {
		appender rollingFile
		console name:'stdout', layout:pattern(conversionPattern: '%d{ABSOLUTE}:%p:%c: %m%n')
	}
	
    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'

    warn   'org.mortbay.log'
	
	environments {
		development {
			root {
				error 'file', 'stdout'
			}
			debug 'grails.app', 'file'
			debug 'au.org.emii', 'file'
		}
		test {
			root {
				error 'file', 'stdout'
			}
			info 'grails.app'
			info 'au.org.emii'
		}
		production {
			root {
				error 'file', 'stdout'
			}
			info 'grails.app', 'file'
			info 'au.org.emii', 'file'
		}
	}
}

// Custom configuration settings
geonetwork.request.template.file = "geonetworkRequestTemplate.ftl"
geonetwork.request.protocol = "OGC:WMS-1.1.1-http-get-map or OGC:WMS-1.3.0-http-get-map"
geonetwork.link.protocol.regex = 'OGC:WMS-1\\.(1\\.1|3\\.0)-http-get-map'
geonetwork.search.page.size = 200
feature.collection.slice.size = 100

grails.gorm.default.mapping = {
   'user-type'(type:org.hibernatespatial.GeometryUserType, class:com.vividsolutions.jts.geom.Geometry)
   'user-type'(type:org.hibernatespatial.GeometryUserType, class:com.vividsolutions.jts.geom.GeometryCollection)
   'user-type'(type:org.hibernatespatial.GeometryUserType, class:com.vividsolutions.jts.geom.LineString)
   'user-type'(type:org.hibernatespatial.GeometryUserType, class:com.vividsolutions.jts.geom.Point)
   'user-type'(type:org.hibernatespatial.GeometryUserType, class:com.vividsolutions.jts.geom.Polygon)
   'user-type'(type:org.hibernatespatial.GeometryUserType, class:com.vividsolutions.jts.geom.MultiLineString)
   'user-type'(type:org.hibernatespatial.GeometryUserType, class:com.vividsolutions.jts.geom.MultiPoint)
   'user-type'(type:org.hibernatespatial.GeometryUserType, class:com.vividsolutions.jts.geom.MultiPolygon)
   'user-type'(type:org.hibernatespatial.GeometryUserType, class:com.vividsolutions.jts.geom.LinearRing)
   'user-type'(type:org.hibernatespatial.GeometryUserType, class:com.vividsolutions.jts.geom.Puntal)
   'user-type'(type:org.hibernatespatial.GeometryUserType, class:com.vividsolutions.jts.geom.Lineal)
   'user-type'(type:org.hibernatespatial.GeometryUserType, class:com.vividsolutions.jts.geom.Polygonal)
}

if(!grails.config.locations || !(grails.config.locations instanceof List)) {
	grails.config.locations = []
}

try {
	configurationPath = new InitialContext().lookup("java:comp/env/aodn.configuration")
	grails.config.locations << "file:${configurationPath}"
}
catch (e) {
}
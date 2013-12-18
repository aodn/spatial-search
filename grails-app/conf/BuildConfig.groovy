
/*
 * Copyright 2012 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.project.dependency.resolution = {
    inherits("global") {
    }

    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'

    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()
    }
    dependencies {
        compile 'log4j:apache-log4j-extras:1.0'
        runtime 'postgresql:postgresql:9.0-801.jdbc4'
    }
}
grails.war.resources = {
    stagingDir ->

    // The jars are being inserted by the hudson/tomcat build process, and
    // are causing errors on startup for the app on tomcat6.
    delete(file:"${stagingDir}/WEB-INF/lib/commons-collections-3.1.jar")
    delete(file:"${stagingDir}/WEB-INF/lib/slf4j-api-1.5.2.jar")

    delete(file:"${stagingDir}/WEB-INF/lib/servlet-api-2.3.jar")

}

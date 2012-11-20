
/*
 * Copyright 2012 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
//grails.project.war.file = "target/${appName}-${appVersion}.war"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()

        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenLocal()
        //mavenCentral()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

		compile 'log4j:apache-log4j-extras:1.0'
		
        // runtime 'mysql:mysql-connector-java:5.1.13'
		runtime 'postgresql:postgresql:9.0-801.jdbc4'
		
    }
}
grails.war.resources = {
   stagingDir ->
   
	 // Container provided...
	 delete(file:"${stagingDir}/WEB-INF/lib/mail-1.4.3.jar")
	 delete(file:"${stagingDir}/WEB-INF/lib/postgis-jdbc-1.3.3.jar")
	 delete(file:"${stagingDir}/WEB-INF/lib/postgresql-9.0-801.jdbc4.jar")

	 // The jars are being inserted by the hudson/tomcat build process, and
	 // are causing errors on startup for the app on tomcat6.
	 delete(file:"${stagingDir}/WEB-INF/lib/commons-collections-3.1.jar")
	 delete(file:"${stagingDir}/WEB-INF/lib/slf4j-api-1.5.2.jar")

	 delete(file:"${stagingDir}/WEB-INF/lib/servlet-api-2.3.jar")
	 
	 copy(toDir: "${stagingDir}/WEB-INF/classes/instances") {
		 fileset(dir: "instances")
	 }
}

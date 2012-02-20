package au.org.emii.search

import grails.test.*

import org.codehaus.groovy.grails.commons.ConfigurationHolder;
import org.codehaus.groovy.grails.commons.GrailsApplication

class GeoNetworkRequestServiceTests extends GrailsUnitTestCase {
	
	def config
	def responseXml
	def service
	
    protected void setUp() {
        super.setUp()
		def config = new ConfigObject()
		config.geonetwork.feature.type.indentifier.regex = 'topp:'
		config.geonetwork.link.protocol.regex = 'OGC:WMS-1\\.(1\\.1|3\\.0)-http-get-map'
		config.geonetwork.search.list.params.items = ['themekey', 'category', 'orgName', 'dataparam', 'longParamName']
		config.geonetwork.search.list.params.delimiter = ','
		ConfigurationHolder.config = config
		GrailsApplication.metaClass.getConfig = {-> config }
		
		responseXml = """<response from="1" to="10" selected="0">
		<summary count="50" type="local" hitsusedforsummary="50">
			<dataParameters/>
			<keywords/>
			<organizationNames/>
		</summary>
</response>"""
		
		service = new GeoNetworkRequestService()
		service.grailsApplication = ConfigurationHolder.config
    }

    protected void tearDown() {
        super.tearDown()
    }
	
	void testGetNumericParam() {
		def params = ['from' : '1', 'to' : '10']
		assertEquals 10, service._getNumericParam(params, 'to')
	}
	
	void testGetPageSize() {
		def params = ['from' : '1', 'to' : '10']
		assertEquals 10, service._getPageSize(params)
	}
	
	void testUpdateNumericParam() {
		def params = ['from' : '1', 'to' : '10']
		service._updateNumericParam(params, 'to', 50)
		assertEquals '50', params['to']
	}
	
	void testPageForward() {
		def geoNetworkResponse = new GeoNetworkResponse(config, responseXml)
		def params = ['from' : '1', 'to' : '10']
		for (def i = 1; i < 40; i += 10) {
			assertTrue service._pageForward(params, geoNetworkResponse.count)
		}
		assertFalse service._pageForward(params, geoNetworkResponse.count)
		assertEquals '41', params.from
		assertEquals '50', params.to
	}
	
	void testIsBoundingBoxSubmitted() {
		def params = [:]
		assertFalse service._isBoundingBoxSubmitted(params)
		params.northBL = '20'
		params.eastBL = '20'
		params.southBL = '10'
		params.westBL = '10'
		assertTrue service._isBoundingBoxSubmitted(params)
	}
	
	void testIsForced() {
		def params = [:]
		assertFalse service._isForced(params)
		
		params.force = false
		assertFalse service._isForced(params)
		
		params.force = 'false'
		assertFalse service._isForced(params)
		
		params.force = true
		assertTrue service._isForced(params)
		
		params.force = 'true'
		assertTrue service._isForced(params)
	}
	
	void testListifyKnownListParams() {
		/*
		 * The configuration mocking doesn't appear to be working at test
		 * time, it sees a ConfigObject that has no contains(Object) method
		 * which is fair enough. I need to improve integration testing so they
		 * can be run more efficiently
		 */
//		def params = ['themekey': 'foo,bar,sink,plug', 'fake1': 'some,more,comma,separated,values']
//		service._listifyKnownListParams(params)
//		assertEquals "bar", params.themekey[1]
//		assertEquals(['foo', 'bar', 'sink', 'plug'], params.themekey)
//		assertEquals "some,more,comma,separated,values", params.fake1
	}
}

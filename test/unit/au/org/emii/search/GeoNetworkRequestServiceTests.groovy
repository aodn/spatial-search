package au.org.emii.search

import grails.test.*

class GeoNetworkRequestServiceTests extends GrailsUnitTestCase {
    def responseXml
	def service
	
    protected void setUp() {
        super.setUp()
		_mockConfig()
		
		responseXml = """<response from="1" to="10" selected="0">
		<summary count="50" type="local" hitsusedforsummary="50">
			<dataParameters/>
			<keywords/>
			<organizationNames/>
		</summary>
</response>"""
		
		service = new GeoNetworkRequestService()
		service.metaClass.getGrailsApplication = { -> [config: org.codehaus.groovy.grails.commons.ConfigurationHolder.config]}
		service.grailsApplication = service.getGrailsApplication()
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
		def geoNetworkResponse = new GeoNetworkResponse(service.grailsApplication.config, responseXml)
		def params = ['from' : '1', 'to' : '10']
		for (def i = 1; i < 1000; i += 200) {
			assertTrue service._pageForward(params, 900)
		}
		assertFalse service._pageForward(params, 900)
		assertEquals '811', params.from
		assertEquals '1010', params.to
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
	
	void testGetPageEnd() {
		def pageEnd = service.grailsApplication.config.geonetwork.search.page.size
		def params = [:]
		params.to = 15
		assertEquals(pageEnd, service._getPageEnd(params))
		
		params.to = pageEnd - 1
		assertEquals(pageEnd, service._getPageEnd(params))
		
		params.to = 200
		assertEquals(200 + pageEnd, service._getPageEnd(params))
		
		params.to = 599
		assertEquals(599 + pageEnd, service._getPageEnd(params))
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
	
	def _mockConfig() {
		mockConfig("geonetwork.feature.type.indentifier.regex = 'topp:'")
		mockConfig("geonetwork.link.protocol.regex = 'OGC:WMS-1\\\\.(1\\\\.1|3\\\\.0)-http-get-map'")
		mockConfig("geonetwork.search.list.params.items = ['themekey', 'category', 'orgName', 'dataparam', 'longParamName']")
		mockConfig("geonetwork.search.list.params.delimiter = ','")
		mockConfig("geonetwork.search.page.size = 50")
	}
}

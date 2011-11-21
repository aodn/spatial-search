package au.org.emii.search

import grails.test.*

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
		GrailsApplication.metaClass.getConfig = {-> config }
		
		responseXml = """<response from="1" to="10" selected="0">
		<summary count="50" type="local" hitsusedforsummary="50">
			<dataParameters/>
			<keywords/>
			<organizationNames/>
		</summary>
</response>"""
		
		service = new GeoNetworkRequestService()
    }

    protected void tearDown() {
        super.tearDown()
    }
	
	def testGetNumericParam() {
		def params = ['from' : '1', 'to' : '10']
		assertEquals 10, service._getNumericParam(params, 'to')
	}
	
	def testGetPageSize() {
		def params = ['from' : '1', 'to' : '10']
		assertEquals 10, service._getPageSize(params)
	}
	
	def testUpdateNumericParam() {
		def params = ['from' : '1', 'to' : '10']
		service._updateNumericParam(params, 'to', 50)
		assertEquals '50', params['to']
	}
	
	def testPageForward() {
		def geoNetworkResponse = new GeoNetworkResponse(config, responseXml)
		def params = ['from' : '1', 'to' : '10']
		for (def i = 1; i < 40; i += 10) {
			assertTrue service._pageForward(params, geoNetworkResponse.count)
		}
		assertFalse service._pageForward(params, geoNetworkResponse.count)
		assertEquals '41', params.from
		assertEquals '50', params.to
	}
	
	def testIsBoundingBoxSubmitted() {
		def params = [:]
		assertFalse service._isBoundingBoxSubmitted(params)
		params.northBL = '20'
		params.eastBL = '20'
		params.southBL = '10'
		params.westBL = '10'
		assertTrue service._isBoundingBoxSubmitted(params)
	}
	
	def testIsForced() {
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
}

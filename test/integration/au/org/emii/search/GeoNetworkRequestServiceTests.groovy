package au.org.emii.search

import grails.test.*
import au.org.emii.search.index.GeonetworkMetadata

class GeoNetworkRequestServiceTests extends SpatialSearchingTest {
    
	protected void setUp() {
        super.setUp()
		_queue(false)
		_index(false, ['topp:argo_float'])
    }

    protected void tearDown() {
        super.tearDown()
    }

//    void testQueue() {
//		def metadata = _queue(true)
//		assertTrue !metadata.isEmpty()
//    }
	
	/*
	 * This method can be time and resource intensive
	 */
//	void testFullIndex() {
//		def metadata = _queue(true)
//		_index(true, null)
//	}
	
	void testNonSpatialSearch() {
		def params = _getSearchParams()
		_addPagingParams(params)
		
		assertTrue params.containsKey('to')

		def results = _search(params)
		def xml = new XmlSlurper().parseText(results)
		assertTrue xml.summary.@count.toInteger() > 0
	}
	
	void testEmptyGeonetworkResponseSpatialSearch() {
		def params = ['themekey' : 'foo']
		_addPagingParams(params)
		def results = _spatialSearch(params, _getAustraliaBounds())
		
		def xml = new XmlSlurper().parseText(results)
		assertEquals 0, xml.summary.@count.toInteger()
	}
	
	void testEmptyPaginatedResponseSpatialSearch() {
		def params = [:]
		_addPagingParams(params)
		def result = _spatialSearch(params, _getExclusiveBounds())
		def xml = new XmlSlurper().parseText(result)
		assertEquals 0, xml.summary.@count.toInteger()
		assertTrue 15 < params.to.toInteger()
	}
	
	void testEmptyResponseSearch() {
		def params = ['themekey' : 'foo']
		def results = _search(params)
		def xml = new XmlSlurper().parseText(results)
		assertEquals 0, xml.summary.@count.toInteger()
	}
	
	def testForceQueue() {
		geoNetworkRequestService.queue([:])
		// Index just gliders for now
		_index(true, ['topp:anfog_glider'])
		def metadata = geoNetworkRequestService.queue(['force' : true])
		assertTrue metadata.size() > 0
	}
	
	def testExtraPaginationResponse() { 
		_queue(true)
		_index(true, ['topp:argo_float'])
		
		// Check that the search has had to paginate further than the supplied
		// to parameter
		def params = ['from' : '1', 'to' : '15']
		def result = _spatialSearch(params, _getAustraliaBounds())
		println result
		def xml = new XmlSlurper().parseText(result)
		assertTrue 15 < xml.@to.toInteger()
		assertTrue 15 < params.to.toInteger()
	}
	
	def _addPagingParams(params) {
		params.putAll(['from' : '1', 'to' : '15'])
		return params
	}
	
	def _addSpatialSearchParams(params, bounds) {
		params.protocol = grailsApplication.config.geonetwork.request.protocol
		params.putAll(['northBL' : bounds[0], 'eastBL' : bounds[1], 'southBL' : bounds[2], 'westBL' : bounds[3]])
		return params
	}
	
	def _getSearchParams() {
		return ['themekey' : 'Fluorometers']
	}
	
	def _getAustraliaBounds() {
		return ['-39.203659', '158.978485', '-54.640301', '144.607330']
	}
	
	def _getAntiMeridianBounds() {
		return ['-39.203659', '-158.978485', '-54.640301', '158.978485']
	}
	
	def _getExclusiveBounds() {
		return ['10', '0', '-10', '10']
	}
	
	def _getInclusiveBounds() {
		return ['0', '0', '-90', '180']
	}
	
	def _spatialSearch(params, bounds) {
		_addSpatialSearchParams(params, bounds)
		return _search(params)
	}
	
	def _search(params) {
		return geoNetworkRequestService.search(params)
	}
}

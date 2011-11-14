package au.org.emii.search

import au.org.emii.search.index.GeonetworkMetadata;
import grails.test.*

class GeoNetworkRequestServiceTests extends GroovyTestCase {
    
	def geoNetworkRequestService
	def featureTypeRequestService
	def grailsApplication
	def _queuedMetadata
	def _indexed = false
	
	protected void setUp() {
        super.setUp()
		_queue(false)
		_index(false, ['topp:anfog_glider'])
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testQueue() {
		def metadata = _queue()
		assertTrue !metadata.isEmpty()
    }
	
	void testFullIndex() {
		def metadata = _queue()
		_index(null)
	}
	
	void testNonSpatialSearch() {
		def params = _getSearchParams()
		_addPagingParams(params)
		
		assertTrue params.containsKey('to')
		assertTrue params.containsKey('westBL')
		
		def results = _search(params)
		
//		def geoNetworkResponse = new GeoNetworkResponse(grailsApplication, results)
//		def metadata = geoNetworkResponse.getGeonetworkMetadataObjects()
//		assertTrue metadata.size() > 0
		
		def xml = new XmlSlurper().parseText(results)
		assertTrue xml.summary.@count.toInteger() > 0
	}
	
	void testEmptyGeonetworkResponseSpatialSearch() {
		def params = ['themekey' : 'foo']
		_addPagingParams(params)
		def results = _spatialSearch(params, [])
		
		def xml = new XmlSlurper().parseText(results)
		assertEquals 0, xml.summary.@count.toInteger()
	}
	
	void testEmptyPaginatedResponseSpatialSearch() {
		def params = ['themekey' : 'Fluorometers']
	}
	
	void testEmptyResponseSearch() {
		def params = ['themekey' : 'foo']
		def results = _search(params)
		def xml = new XmlSlurper().parseText(results)
		assertEquals 0, xml.summary.@count.toInteger()
	}
	
	def _addPagingParams(params) {
		params.putAll(['from' : '1', 'to' : '15'])
		return params
	}
	
	def _addSpatialSearchParams(params, bounds) {
		params.putAll(['northBL' : bounds[0], 'eastBL' : bounds[1], 'southBL' : bounds[2], 'westBL' : bounds[3]])
		return params
	}
	
	def _getSearchParams() {
		return ['themekey' : 'Fluorometers']
	}
	
	def _getAustraliaBounds() {
		
	}
	
	def _getAntiMeridianBounds() {
		
	}
	
	def _getExclusiveBounds() {
		
	}
	
	def _spatialSearch(params, bounds) {
		_addSpatialSearchParams(params, bounds)
		return _search(params)
	}
	
	def _search(params) {
		return geoNetworkRequestService.search(params)
	}
	
	def _queue(refresh) {
		if (!_queuedMetadata || refresh) {
			_queuedMetadata = geoNetworkRequestService.queue([:])
		}
		return _queuedMetadata
	}
	
	def _index(refresh, featureTypeNames) {
		// Only index a subset of features when specified
		if (featureTypeNames) {
			def criteria = GeonetworkMetadata.createCriteria()
			def l = criteria.list {
				not {
					'in'('featureTypeName', featureTypeNames)
				}
			}
			l*.delete()
		}
		if (!_indexed || refresh || featureTypeNames) {
			featureTypeRequestService.index()
		}
	}
}

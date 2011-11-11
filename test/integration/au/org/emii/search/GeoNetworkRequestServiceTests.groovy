package au.org.emii.search

import au.org.emii.search.index.GeonetworkMetadata;
import grails.test.*

class GeoNetworkRequestServiceTests extends GroovyTestCase {
    
	def geoNetworkRequestService
	def featureTypeRequestService
	def grailsApplication
	
	protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testQueue() {
		def metadata = _queue()
		assertTrue !metadata.isEmpty()
    }
	
	void testSearch() {
		def metadata = _queue()
		def l = GeonetworkMetadata.findAllByFeatureTypeNameNotEqual('topp:anfog_glider')
		println l.size()
		l*.delete()
		println l.size()
		
		def gliders = GeonetworkMetadata.findAll()
		println gliders.size()
		assertFalse gliders.isEmpty()
	}
	
	def _queue() {
		return geoNetworkRequestService.queue([:])
	}
}

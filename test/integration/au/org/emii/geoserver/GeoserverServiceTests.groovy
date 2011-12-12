package au.org.emii.geoserver

import org.apache.http.impl.client.BasicResponseHandler;

import au.org.emii.search.GetRequest;
import grails.test.*

class GeoserverServiceTests extends GroovyTestCase {
    
	def aims = 'http://maps.aims.gov.au/geoserver/wfs?service=wfs&version=1.1.0&request=GetCapabilities'
	
	protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testGeoserverServiceInstantiation() {
		def request = new GetRequest()
		def httpResponse = request.request(aims, new BasicResponseHandler())
		def geo = new GeoserverService(httpResponse)
		
		assertEquals 'Australian Institute of Marine Science', geo.provider
		assertEquals 'Greg Coleman', geo.individual
		assertEquals 'Data Manager / Programmer', geo.position
		assertTrue geo.operations.size() > 0
    }
}

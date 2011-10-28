package au.org.emii.search.index

import com.vividsolutions.jts.io.WKTReader;

import grails.test.*

class FeatureTypeRequestServiceTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testToLineString() {
		//def featureTypeRequest = new AnfogGliderRequest()
		//def geometry = new WKTReader().read('LINESTRING (0 0, 10 0, 20 0)')
		//assertEquals geometry, featureTypeRequest.toGeometry('0 0 0 10 0 20')
    }
	
	void testToPoint() {
		//def featureTypeRequest = new AnfogGliderRequest()
		//def geometry = new WKTReader().read('POINT (180.0 -44.2876957336702)')
		//assertEquals geometry, featureTypeRequest.toGeometry('-44.2876957336702 180.0')
	}
}

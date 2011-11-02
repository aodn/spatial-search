package au.org.emii.search.geometry

import grails.test.*

class GeometryHelperTests extends GrailsUnitTestCase {
    
	def helper = new GeometryHelper()
	
	protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testToPoint() {
		def s = '-43.897892 132.736816'
		def g = helper.toGeometry('Point', s)
		assertEquals 'com.vividsolutions.jts.geom.Point', g.getClass().getName()
		assertEquals 132.736816d, g.getX()
    }
	
	void testToLineString() {
		def s = '-43.897892 132.736816 -43.897892 140.537109 -42.3475345 148.337402 -40.797177 140.537109 -40.797177 132.736816 -42.3475345 124.936523'
		def g = helper.toGeometry('LineString', s)
		assertEquals 'com.vividsolutions.jts.geom.LineString', g.getClass().getName()
	}
	
	void testToCurve() {
		def s = '-43.897892 132.736816 -43.897892 140.537109 -42.3475345 148.337402 -40.797177 140.537109 -40.797177 132.736816 -42.3475345 124.936523'
		def g = helper.toGeometry('Curve', s)
		assertEquals 'com.vividsolutions.jts.geom.LineString', g.getClass().getName()
	}
	
	void testToPolygon() {
		def s = '-43.897892 132.736816 -43.897892 140.537109 -42.3475345 148.337402 -40.797177 140.537109 -40.797177 132.736816 -42.3475345 124.936523 -43.897892 132.736816'
		def g = helper.toGeometry('Polygon', s)
		assertEquals 'com.vividsolutions.jts.geom.Polygon', g.getClass().getName()
	}
}

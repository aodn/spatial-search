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
	
	void testToMultiPolygonWkt() {
		def l = [
			['40 40 20 45 45 30 40 40'], 
			['-43.897892 132.736816 -43.897892 140.537109 -42.3475345 148.337402 -40.797177 140.537109 -40.797177 132.736816 -42.3475345 124.936523 -43.897892 132.736816', '0 10 0 20 10 20 10 10 0 10'], 
		    ['20 35 45 20 30 5 10 10 10 30 20 35', '30 20 20 25 20 15 30 20']
		]
		def s = 'MULTIPOLYGON (((40 40, 45 20, 30 45, 40 40)), ((132.736816 -43.897892, 140.537109 -43.897892, 148.337402 -42.3475345, 140.537109 -40.797177, 132.736816 -40.797177, 124.936523 -42.3475345, 132.736816 -43.897892), (10 0, 20 0, 20 10, 10 10, 10 0)), ((35 20, 20 45, 5 30, 10 10, 30 10, 35 20), (20 30, 25 20, 15 20, 20 30)))'
		def wkt = helper._toMultiPolygon(l)
		assertEquals s, wkt
	}
	
	void testToMultiPolygon() {
		def l = [
			['40 40 20 45 45 30 40 40'],
			['-43.897892 132.736816 -43.897892 140.537109 -42.3475345 148.337402 -40.797177 140.537109 -40.797177 132.736816 -42.3475345 124.936523 -43.897892 132.736816', '0 10 0 20 10 20 10 10 0 10'],
			['20 35 45 20 30 5 10 10 10 30 20 35', '30 20 20 25 20 15 30 20']
		]
		def g = helper.toGeometry('MultiPolygon', l)
		assertEquals 'com.vividsolutions.jts.geom.MultiPolygon', g.getClass().getName()
	}
	
	void testMultiPolygonWithHole() {
		def l = [
			['40 40 45 20 30 45 40 40'],
			['35 20 20 45 5 30 10 10 30 10 35 20', '20 30 25 20 15 20 20 30']
		]
		
		def s = 'MULTIPOLYGON (((40 40, 20 45, 45 30, 40 40)), ((20 35, 45 20, 30 5, 10 10, 10 30, 20 35), (30 20, 20 25, 20 15, 30 20)))'
		def wkt = helper._toMultiPolygon(l)
		assertEquals s, wkt
	}
	
	void testToBoundingBox() {
		def north = '1.37'
		def east = '168.16'
		def south = '-51.37'
		def west = '97.84'
		
		def bb = helper.toBoundingBox(north, east, south, west)
		assertEquals 'com.vividsolutions.jts.geom.Polygon', bb.getClass().getName()
		
		// If we cross the anti-meridian we expect a multipolygon
		east = '-178'
		bb = helper.toBoundingBox(north, east, south, west)
		assertEquals 'com.vividsolutions.jts.geom.MultiPolygon', bb.getClass().getName()
	}
}

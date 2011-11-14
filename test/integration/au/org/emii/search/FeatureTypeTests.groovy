package au.org.emii.search

import grails.test.*

import org.hibernatespatial.criterion.SpatialRestrictions

import au.org.emii.search.geometry.GeometryHelper

class FeatureTypeTests extends SpatialSearchingTest {
    
	protected void setUp() {
        //super.setUp()
		_queue(false)
		_index(false, ['topp:soop_sst_without_1min_vw', 'topp:argo_float'])
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testPolygonIntersection() {
		def geomHelper = new GeometryHelper()
		def p = geomHelper.toGeometry('Polygon', '-43.897892 132.736816 -43.897892 140.537109 -42.3475345 148.337402 -40.797177 140.537109 -40.797177 132.736816 -42.3475345 124.936523 -43.897892 132.736816')
		def c = FeatureType.createCriteria()
		def l = c.list {
			add(SpatialRestrictions.intersects('geometry', p))
		}
		assertTrue(l.size() > 0)
    }
	
	void testAntiMeridian() {
		// -38.069000244140625 -179.06300354003906
		// soop_sst_without_1min_vw
		def geomHelper = new GeometryHelper()
		def p = geomHelper.toGeometry('MultiPolygon', [['-38 178 -38 180 -40 180 -40 178 -38 178', '-40 -180 -40 -178 -38 -178 -38 -180 -40 -180']])
		def c = FeatureType.createCriteria()
		def l = c.list {
			add(SpatialRestrictions.intersects('geometry', p))
		}
		assertTrue(l.size() > 0)
	}
}

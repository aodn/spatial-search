package au.org.emii.search

import grails.test.*

import org.hibernatespatial.criterion.SpatialRestrictions

import au.org.emii.search.geometry.GeometryHelper
import au.org.emii.search.index.FeatureTypeRequest

import com.vividsolutions.jts.geom.GeometryFactory
import com.vividsolutions.jts.io.WKTReader

class FeatureTypeTests extends GroovyTestCase {
    
	protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testPolygonIntersection() {
		def geomHelper = new GeometryHelper()
		def p = geomHelper.toGeometry('Polygon', '-43.897892 132.736816, -43.897892 140.537109, -42.3475345 148.337402, -40.797177 140.537109, -40.797177 132.736816, -42.3475345 124.936523, -43.897892 132.736816')
		def c = FeatureType.createCriteria()
		def l = c.list {
			add(SpatialRestrictions.intersects('geometry', p))
		}
		
		assertTrue(l.size() > 0)
    }
}

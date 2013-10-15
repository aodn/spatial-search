
/*
 * Copyright 2012 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */
package au.org.emii.search

import grails.test.*

class FeatureTypeTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
        mockLogging(FeatureType)
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testGetSridUrn() {
        def featureType = new FeatureType()
        featureType.gml = '''<gml:Point srsDimension="2" srsName="urn:x-ogc:def:crs:EPSG:4283"><gml:pos>-18.52 146.39</gml:pos></gml:Point>'''
        assertEquals "4283", featureType.getSridFromGml()
    }

    void testGetSridHttp() {
        def featureType = new FeatureType()
        featureType.gml = '''<gml:Point srsDimension="2" srsName="http://www.opengis.net/gml/srs/epsg.xml#4326"><gml:pos>-18.52 146.39</gml:pos></gml:Point>'''
        assertEquals "4326", featureType.getSridFromGml()
    }

    void testGetSridDefault() {
        def featureType = new FeatureType()
        featureType.gml = '''<gml:Point srsDimension="2" srsName="some non sensical string"><gml:pos>-18.52 146.39</gml:pos></gml:Point>'''
        assertEquals "4326", featureType.getSridFromGml()
    }
}

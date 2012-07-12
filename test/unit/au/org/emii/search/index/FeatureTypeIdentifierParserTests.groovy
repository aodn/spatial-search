package au.org.emii.search.index

import grails.test.*
import groovy.xml.MarkupBuilder;

class FeatureTypeIdentifierParserTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testParseFromNode() {
		
		def out = new StringWriter()
		def builder = new MarkupBuilder(new PrintWriter(out))."wfs:FeatureCollection"(
			"xmlns:ogc": "http://www.opengis.net/ogc", 
			"xmlns:helpers": "helpers", 
			"xmlns:AATAMS": "aatams", 
			"xmlns:aodn": "aodn", 
			"xmlns:wfs": "http://www.opengis.net/wfs", 
			"xmlns:topp": "http://www.openplans.org/topp", 
			"xmlns:imos": "imos", 
			"xmlns:baselayers": "baselayers", 
			"xmlns:xsi": "http://www.w3.org/2001/XMLSchema-instance", 
			"xmlns:ows": "http://www.opengis.net/ows", 
			"xmlns:gml": "http://www.opengis.net/gml", 
			"xmlns:xlink": "http://www.w3.org/1999/xlink", 
			numberOfFeatures: 1, 
			timeStamp: "2012-07-12T09:37:10.497+10:00", 
			"xsi:schemaLocation": "imos http://geoserver.imos.org.au:80/geoserver/wfs?service=WFS&version=1.1.0&request=DescribeFeatureType&typeName=imos%3Asoop_ba_mv http://www.opengis.net/wfs http://geoserver.imos.org.au:80/geoserver/schemas/wfs/1.1.0/wfs.xsd"
		) {
			"gml:featureMembers" {
				"imos:soop_ba_mv"("gml:id": "soop_ba_mv.fid--5581107b_1387853ccab_-7f77") {
					"imos:id"('410427')
					"imos:geometry" {
						"gml:LineString"(srsDimension: 2, srsName: "http://www.opengis.net/gml/srs/epsg.xml#4326") {
							"gml:posList"("78.05901748 -42.64454993 78.05708812 -42.63787697 78.05454935 -42.62909627 78.05240843 -42.62039899 78.05125952 -42.61133221 78.05012069 -42.60234495 78.04898187 -42.59335772 78.04785312 -42.58445 78.04671429 -42.57546273 78.04557546 -42.56647551 78.04444671 -42.55756779 78.04330788 -42.54858052 78.04216905 -42.53959325 78.04104023 -42.53068494 78.04120553 -42.52168092 78.0419248 -42.51269 78.04264371 -42.50370367 78.04336261 -42.49471737 78.04408152 -42.48573104 78.04485086 -42.47667211 78.04585683 -42.4678177 78.0468806 -42.4588066 78.04789547 -42.44987386 78.04891924 -42.44086275 78.0499341 -42.43193002 78.05094897 -42.42299724 78.05197274 -42.41398617 78.05298761 -42.4050534")
						}
					}
				}
			}
		}
		println out
		
		def idParser = new FeatureTypeIndentifierParser()
		def node = new XmlSlurper(false, true).parseText(out.toString())
		
		assertEquals "410427", idParser.parseFromNode("id", node.featureMembers[0].children()[0])
    }
}

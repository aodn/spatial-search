package au.org.emii.search.test.unit

import au.org.emii.search.GeoNetworkLink;
import grails.test.*

class GeoNetworkLinkTests extends GrailsUnitTestCase {
    
	protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }
	
	void testEmptyLinkXml() {
		assertNull(new GeoNetworkLink(null).featureType)
		assertNull(new GeoNetworkLink("").featureType)
	}

    void testLink() {
		def linkXml = """imos:pciview|SOOP CPR Plankton Colour Index Survey|http://imos2.ersa.edu.au/geo2/imos/wms|OGC:WMS-1.1.1-http-get-map|application/vnd.ogc.wms_xml"""
		def link = new GeoNetworkLink(linkXml)
		assertEquals("imos:pciview", link.featureType)
		assertEquals("SOOP CPR Plankton Colour Index Survey", link.title)
		assertEquals("http://imos2.ersa.edu.au/geo2/imos/wms", link.href)
		assertEquals("OGC:WMS-1.1.1-http-get-map", link.protocol)
		assertEquals("application/vnd.ogc.wms_xml", link.mimeType)
    }
}

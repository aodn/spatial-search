
/*
 * Copyright 2012 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */
package au.org.emii.search.test.unit

import org.codehaus.groovy.grails.commons.ConfigurationHolder;
import org.codehaus.groovy.grails.commons.DefaultGrailsApplication;
import org.codehaus.groovy.grails.commons.GrailsApplication;

import au.org.emii.search.GeoNetworkLink;
import grails.test.*

class GeoNetworkLinkTests extends GrailsUnitTestCase {
    
	def grailsApplication = new DefaultGrailsApplication()
	
	protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }
	
	void testEmptyLinkXml() {
		assertNull(new GeoNetworkLink(grailsApplication, null).featureType)
		assertNull(new GeoNetworkLink(grailsApplication, "").featureType)
	}

    void testLink() {
		def link = new GeoNetworkLink(grailsApplication, """imos:pciview|SOOP CPR Plankton Colour Index Survey|http://imos2.ersa.edu.au/geo2/imos/wms|OGC:WMS-1.1.1-http-get-map|application/vnd.ogc.wms_xml""")
		assertEquals("imos:pciview", link.featureType)
		assertEquals("SOOP CPR Plankton Colour Index Survey", link.title)
		assertEquals("http://imos2.ersa.edu.au/geo2/imos/wms", link.href)
		assertEquals("OGC:WMS-1.1.1-http-get-map", link.protocol)
		assertEquals("application/vnd.ogc.wms_xml", link.mimeType)
    }
	
	void testIsProtocol() {
		def link = new GeoNetworkLink(grailsApplication, """imos:pciview|SOOP CPR Plankton Colour Index Survey|http://imos2.ersa.edu.au/geo2/imos/wms|OGC:WMS-1.3.0-http-get-map|application/vnd.ogc.wms_xml""")
		assertTrue(link._isProtocol())
	}
	
	void testIsNotProtocol() {
		def link = new GeoNetworkLink(grailsApplication, """imos:pciview|SOOP CPR Plankton Colour Index Survey|http://imos2.ersa.edu.au/geo2/imos/wms|WWW:DOWNLOAD-1.0-http--download|application/vnd.ogc.wms_xml""")
		assertFalse(link._isProtocol())
	}
	
	void testIsKmlNotProtocol() {
		def link = new GeoNetworkLink(grailsApplication, """imos:pciview|SOOP CPR Plankton Colour Index Survey|http://imos2.ersa.edu.au/geo2/imos/wms|application/vnd.google-earth.kml+xml|application/vnd.ogc.wms_xml""")
		assertFalse(link._isProtocol())
	}
	
	void testMatchesNcWms() {
		def link = new GeoNetworkLink(grailsApplication, """ACORN_raw_data_CBG_4k_grid/UCUR|eastward_sea_water_velocity (non QC data March 2011 ongoing)|http://ncwms.emii.org.au/ncWMS/wms|OGC:WMS-1.1.1-http-get-map|application/vnd.ogc.wms_xml""")
		assertTrue(link.isMapLink())
		assertEquals(link.href, "http://ncwms.emii.org.au/ncWMS/wms")
	}
}

package au.org.emii.search

import grails.test.*

import org.codehaus.groovy.grails.commons.GrailsApplication

import au.org.emii.search.index.GeonetworkMetadata;

class GeoNetworkResponseTests extends GrailsUnitTestCase {
    
	def geoNetworkResponse

	protected void setUp() {
        super.setUp()
		def config = new ConfigObject()
		config.geonetwork.feature.type.indentifier.regex = 'topp:'
		config.geonetwork.link.protocol.regex = 'OGC:WMS-1\\.(1\\.1|3\\.0)-http-get-map'
		GrailsApplication.metaClass.getConfig = {-> config }
		geoNetworkResponse = new GeoNetworkResponse(config)
    }

    protected void tearDown() {
        super.tearDown()
    }
	
	void testParseLinkElement() {
		def link = new Node(null, "link", null, """imos:pciview|SOOP CPR Plankton Colour Index Survey|http://imos2.ersa.edu.au/geo2/imos/wms|OGC:WMS-1.1.1-http-get-map|application/vnd.ogc.wms_xml""")
		def metadata = geoNetworkResponse._parseLinkElement(link)
		assertNotNull(metadata)
		assertEquals("imos:pciview", metadata.featureTypeName)
		assertEquals("http://imos2.ersa.edu.au/geo2/imos", metadata.geoserverEndPoint)
		assertNull(geoNetworkResponse._parseLinkElement(null))
		assertNull(geoNetworkResponse._parseLinkElement(""))
	}
	
	def _getGliderFeature() {
		mockDomain(FeatureType)
		def featureType = new FeatureType()
		featureType.geonetworkUuid = '02640f4e-08d0-4f3a-956b-7f9b58966ccc'
		featureType.featureTypeName = 'topp:anfog_glider'
		return featureType
	}
}


/*
 * Copyright 2012 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */
package au.org.emii.search

import grails.test.*
import groovy.xml.MarkupBuilder

import org.codehaus.groovy.grails.commons.GrailsApplication

import au.org.emii.search.geometry.GeometryHelper


class GeoNetworkResponseTests extends GrailsUnitTestCase {

	def geoNetworkResponse
	def helper = new GeometryHelper()

	protected void setUp() {
        super.setUp()
		geoNetworkResponse = new GeoNetworkResponse(_mockConfig())
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

	def testParseGeoBox() {
		def out = new StringWriter()
		def builder = new MarkupBuilder(new PrintWriter(out))."response"(from: 1, to: 1, selected: 0) {
			summary(count: 1, type: "local", hitsusedforsummary: 1) {
				keywords {
					keyword(count: 1, name: "Cool keywords", indexKey: "keyword")
				}
			}
			metadata {
				geoBox ("150.8|-24.2|153.6|-21.9")
			}
		}
		println out

		geoNetworkResponse = new GeoNetworkResponse(_mockConfig(), out.toString())
		def metadata = geoNetworkResponse.tree.metadata

		def expected = helper.toBoundingBox("-21.9", "153.6", "-24.2", "150.8")
		assertEquals(expected, geoNetworkResponse._parseGeoBox(metadata))
	}

	def testParseMultipleGeoBoxes() {
		def out = new StringWriter()
		def builder = new MarkupBuilder(new PrintWriter(out))."response"(from: 1, to: 1, selected: 0) {
			summary(count: 1, type: "local", hitsusedforsummary: 1) {
				keywords {
					keyword(count: 1, name: "Cool keywords", indexKey: "keyword")
				}
			}
			metadata {
				geoBox("146.8437|-19.2084|146.8437|-19.2084")
				geoBox("146.4825|-18.6216|146.4825|-18.6216")
				geoBox("148.9443|-20.4464|148.9443|-20.4464")
				geoBox("150.897|-23.1550|150.897|-23.1550")
				geoBox("150.8973|-23.1548|150.8973|-23.1548")
				geoBox("150.9546|-23.0665|150.9546|-23.0665")
				geoBox("150.9667|-23.2058|150.9667|-23.2058")
				geoBox("150.9244|-23.1553|150.9244|-23.1553")
				geoBox("150.9633|-23.2110|150.9633|-23.2110")
				geoBox("152.5566|-21.7059|152.5566|-21.7059")
				geoBox("152.5559|-21.4785|152.5559|-21.4785")
				geoBox("149.4081|-20.3533|149.4081|-20.3533")
				geoBox("146.4825|-18.6156|146.4825|-18.6156")
			}
		}
		println out

		geoNetworkResponse = new GeoNetworkResponse(_mockConfig(), out.toString())
		def metadata = geoNetworkResponse.tree.metadata

		//North, east, south, west

		def expected = helper.toGeometryFromCoordinateText('MultiPoint', [
			["-19.2084 146.8437 -19.2084 146.8437"],
			["-18.6216 146.4825 -18.6216 146.4825"],
			["-20.4464 148.9443 -20.4464 148.9443"],
			["-23.1550 150.897 -23.1550 150.897"],
			["-23.1548 150.8973 -23.1548 150.8973"],
			["-23.0665 150.9546 -23.0665 150.9546"],
			["-23.2058 150.9667 -23.2058 150.9667"],
			["-23.1553 150.9244 -23.1553 150.9244"],
			["-23.2110 150.9633 -23.2110 150.9633"],
			["-21.7059 152.5566 -21.7059 152.5566"],
			["-21.4785 152.5559 -21.4785 152.5559"],
			["-20.3533 149.4081 -20.3533 149.4081"],
			["-18.6156 146.4825 -18.6156 146.4825"]
		])
		assertEquals(expected, geoNetworkResponse._parseGeoBox(metadata))
	}

	def _getGliderFeature() {
		mockDomain(FeatureType)
		def featureType = new FeatureType()
		featureType.geonetworkUuid = '02640f4e-08d0-4f3a-956b-7f9b58966ccc'
		featureType.featureTypeName = 'topp:anfog_glider'
		return featureType
	}

	def _mockConfig() {
		def config = new ConfigObject()
		config.geonetwork.feature.type.indentifier.regex = 'topp:'
		config.geonetwork.link.protocol.regex = 'OGC:WMS-1\\.(1\\.1|3\\.0)-http-get-map'
		GrailsApplication.metaClass.getConfig = {-> config }
		return config
	}
}

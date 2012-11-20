
/*
 * Copyright 2012 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */
package au.org.emii.geoserver.test.unit

import au.org.emii.geoserver.GeoserverOperation;
import grails.test.*

class GeoserverOperationTests extends GrailsUnitTestCase {
    
	def xml = """<Operation name="GetCapabilities">
	<DCP>
	  <HTTP>
		<Get href="http://maps.aims.gov.au/geoserver/wfs"/>
		<Post href="http://maps.aims.gov.au/geoserver/wfs"/>
	  </HTTP>
	</DCP>
	<Parameter name="AcceptVersions">
	  <Value>1.0.0</Value>
	  <Value>1.1.0</Value>
	</Parameter>
	<Parameter name="AcceptFormats">
	  <Value>text/xml</Value>
	</Parameter>
  </Operation>
  """
	
	protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testReadValues() {
		def valXml = """<Parameter name="AcceptVersions">
		  <Value>1.0.0</Value>
		  <Value>1.1.0</Value>
		</Parameter>"""
		
		def slurp = new XmlSlurper().parseText(valXml)
		def op = new GeoserverOperation()
		
		def values = op._readParamValues(slurp)
		assertEquals 2, values.size()
		assertEquals '1.0.0', values[0]
		assertEquals '1.1.0', values[1]
    }
	
	void testGetAndPostLink() {
		def slurp = new XmlSlurper().parseText(xml)
		def op = new GeoserverOperation(slurp)
		assertEquals 'http://maps.aims.gov.au/geoserver/wfs', op.getLink
		assertEquals 'http://maps.aims.gov.au/geoserver/wfs', op.postLink
	}
	
	void testReadParam() {
		def slurp = new XmlSlurper().parseText(xml)
		def op = new GeoserverOperation()
		
		def values = op._readParams(slurp)
		assertEquals 2, op.parameters.size()
		assertTrue op.parameters.containsKey('AcceptVersions')
		assertTrue op.parameters.containsKey('AcceptFormats')
		assertEquals 2, op.parameters['AcceptVersions'].size()
		assertEquals 1, op.parameters['AcceptFormats'].size()
	}
	
	void testOperation() {
		def slurp = new XmlSlurper().parseText(xml)
		def op = new GeoserverOperation(slurp)
		
		assertEquals 'GetCapabilities', op.name
	}
}

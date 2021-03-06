
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

import au.org.emii.search.GeoNetworkKeyword;
import au.org.emii.search.GeoNetworkKeywordSummary;
import grails.test.*

class GeoNetworkKeywordSummaryTests extends GrailsUnitTestCase {

    def temperature = "Oceans | Ocean Temperature | Water Temperature"
    def fluorescence = "Oceans | Ocean Optics | Fluorescence"
    def atmosphere = "Atmosphere | Atmospheric Water Vapor | Humidity"
    def plankton = "Biosphere | Microbiota | Plankton"
    def zooPlankton = "Biosphere | Microbiota | Zooplankton"
    def phytoPlankton = "Biosphere | Microbiota | Phytoplankton"

    def allKeywords = [
        temperature,
        fluorescence,
        atmosphere,
        plankton,
        zooPlankton,
        phytoPlankton
    ]

    def grailsApplication = new DefaultGrailsApplication()

    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testInitialisation() {
        def s = new GeoNetworkKeywordSummary(grailsApplication)
        assertNotNull(s.keywordsMap)
    }

    void testAddKeyword() {
        def s = new GeoNetworkKeywordSummary(grailsApplication)
        _addAllKeywords(s)
        assertEquals(allKeywords.size(), s.keywordsMap.size())
    }

    void testGetKeywords() {
        def s = new GeoNetworkKeywordSummary(grailsApplication)
        _addAllKeywords(s)
        assertEquals(allKeywords.size(), s.getKeywords().size())
    }

    void testDuplicateKeywords() {
        def s = new GeoNetworkKeywordSummary(grailsApplication)
        _addAllKeywords(s)
        _addAllKeywords(s)
        assertEquals(allKeywords.size(), s.getKeywords().size())
    }

    void testGetKeywordsIsSorted() {
        def s = new GeoNetworkKeywordSummary(grailsApplication)
        _addAllKeywords(s)

        def rev = []
        rev.addAll(allKeywords)
        Collections.reverse(rev)

        int i = rev.size()
        rev.each { kw ->
            s._addKeyword(new GeoNetworkKeyword(name: kw, count: i, indexKey: 'someKeyword'))
            i -= 1
        }

        i = rev.size() - 1
        s.getKeywords().each {
            assertEquals(allKeywords[i], it.name)
            i -= 1
        }
    }

    def _addAllKeywords(s) {
        allKeywords.each {
            s._addKeyword(new GeoNetworkKeyword(name: it, indexKey: 'someKeyword'))
        }
    }
}

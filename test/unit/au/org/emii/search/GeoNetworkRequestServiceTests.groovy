
/*
 * Copyright 2012 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */
package au.org.emii.search

import org.codehaus.groovy.grails.commons.DefaultGrailsApplication;

import grails.test.*

class GeoNetworkRequestServiceTests extends GrailsUnitTestCase {
    def responseXml
    def service

    protected void setUp() {
        super.setUp()
        //_mockConfig()

        responseXml = """<response from="1" to="10" selected="0">
        <summary count="50" type="local" hitsusedforsummary="50">
            <dataParameters/>
            <keywords/>
            <organizationNames/>
        </summary>
</response>"""

        service = new GeoNetworkRequestService()
        //service.metaClass.getGrailsApplication = { -> [config: org.codehaus.groovy.grails.commons.ConfigurationHolder.config]}
        service.grailsApplication = new DefaultGrailsApplication()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testGetNumericParam() {
        def params = ['from' : '1', 'to' : '10']
        assertEquals 10, service._getNumericParam(params, 'to')
    }

    void testGetPageSize() {
        def params = ['from' : '1', 'to' : '10']
        assertEquals 10, service._getPageSize(params)
    }

    void testUpdateNumericParam() {
        def params = ['from' : '1', 'to' : '10']
        service._updateNumericParam(params, 'to', 50)
        assertEquals '50', params['to']
    }

    void testPageForward() {
        def geoNetworkResponse = new GeoNetworkResponse(service.grailsApplication.config, responseXml)
        def params = ['from' : '1', 'to' : '10']
        for (def i = 1; i < 1000; i += 200) {
            assertTrue service._pageForward(params, 900)
        }
        assertFalse service._pageForward(params, 900)
        assertEquals '811', params.from
        assertEquals '1010', params.to
    }

    void testIsBoundingBoxSubmitted() {
        def params = [:]
        assertFalse service._isBoundingBoxSubmitted(params)
        _mockBoundingBox(params)
        assertTrue service._isBoundingBoxSubmitted(params)
    }

    void testIsSpatialSearchWithBoundingBox() {
        assertTrue service._isSpatialSearch(_mockBoundingBox([:]))
    }

    void testIsSpatialSearchWithGeometry() {
        assertTrue service._isSpatialSearch(_mockGeometry([:]))
    }

    void testReturnsGeometryWhenSpecified() {
        def params = _mockGeometry(_mockBoundingBox([:]))
        assertEquals params["geometry"], service._getGeometryText(params)
    }

    void testGetPageEnd() {
        def pageEnd = service.grailsApplication.config.geonetwork.search.page.size
        def params = [:]
        params.to = 15
        assertEquals(pageEnd, service._getPageEnd(params))

        params.to = pageEnd - 1
        assertEquals(pageEnd, service._getPageEnd(params))

        params.to = 200
        assertEquals(200 + pageEnd, service._getPageEnd(params))

        params.to = 599
        assertEquals(599 + pageEnd, service._getPageEnd(params))
    }

    def _mockBoundingBox(params) {
        params.northBL = '20'
        params.eastBL = '20'
        params.southBL = '10'
        params.westBL = '10'
        return params
    }

    def _mockGeometry(params) {
        params["geometry"] = "POLYGON((140.44921875 -41.44140625,143.0859375 -46.01171875,154.86328125 -44.60546875,151.34765625 -39.15625,147.65625 -38.8046875,140.44921875 -41.44140625))"
        return params
    }

    def _mockConfig() {
        mockConfig("geonetwork.feature.type.indentifier.regex = 'topp:'")
        mockConfig("geonetwork.link.protocol.regex = 'OGC:WMS-1\\\\.(1\\\\.1|3\\\\.0)-http-get-map'")
        mockConfig("geonetwork.search.list.params.items = ['themekey', 'category', 'orgName', 'dataparam', 'longParamName']")
        mockConfig("geonetwork.search.list.params.delimiter = ','")
        mockConfig("geonetwork.search.page.size = 50")
    }
}

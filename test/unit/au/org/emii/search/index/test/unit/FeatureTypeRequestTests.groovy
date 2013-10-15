
/*
 * Copyright 2012 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */
package au.org.emii.search.index.test.unit

import grails.test.*
import au.org.emii.search.index.FeatureTypeRequest

class FeatureTypeRequestTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testTrimNamespace() {
        def ftr = new FeatureTypeRequest()
        assertEquals 'AIMS_TRIP_5310', ftr.trimNamespace('aims:AIMS_TRIP_5310')
        assertEquals 'anfog_glider', ftr.trimNamespace('topp:anfog_glider')
        assertEquals 'namespace', ftr.trimNamespace('really:long:delimited:thingy:foo:namespace')
    }
}

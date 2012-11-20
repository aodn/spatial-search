
/*
 * Copyright 2012 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */
package au.org.emii.search.index

import java.sql.Timestamp;
import java.util.Calendar;

import grails.test.*

class FeatureTypeRequestServiceTests extends GrailsUnitTestCase {
	
	def service
	
    protected void setUp() {
        super.setUp()
		mockLogging(FeatureTypeRequestService)
		service = new FeatureTypeRequestService()
    }

    protected void tearDown() {
        super.tearDown()
    }
	
	void testSliceSize() {
		mockConfig("feature.collection.slice.size = 10")
		_applyConfig()
		assertEquals(10, service._getSliceSize())
	}
	
	void testInvalidSliceSize() {
		mockConfig("feature.collection.slice.size = foo")
		_applyConfig()
		assertEquals(100, service._getSliceSize())
	}
	
	void testNegativeSliceSize() {
		mockConfig("feature.collection.slice.size = -1")
		_applyConfig()
		assertEquals(100, service._getSliceSize())
	}
	
	void testZeroSliceSize() {
		mockConfig("feature.collection.slice.size = 0")
		_applyConfig()
		assertEquals(100, service._getSliceSize())
	}
	
	void testUnindexedCheck() {
		assertFalse service._unindexed([])
		
		def now = new Timestamp(System.currentTimeMillis())
		assertFalse service._unindexed([new GeonetworkMetadata(changeDate: now, lastIndexed: now)])
		assertTrue service._unindexed([new GeonetworkMetadata(changeDate: now)])
		
		def yesterday = Calendar.getInstance()
		yesterday.add(Calendar.DATE, -1)
		assertTrue service._unindexed([new GeonetworkMetadata(changeDate: now, lastIndexed: new Timestamp(yesterday.timeInMillis))])
		
		assertTrue service._unindexed([new GeonetworkMetadata()])
		
		def metadata = [
			new GeonetworkMetadata(changeDate: now, lastIndexed: now),
			new GeonetworkMetadata(changeDate: now, lastIndexed: new Timestamp(yesterday.timeInMillis)),
			new GeonetworkMetadata(changeDate: now, lastIndexed: now),
		]
		assertTrue service._unindexed(metadata)
	}
	
	def _applyConfig() {
		service.metaClass.getGrailsApplication = { -> [config: org.codehaus.groovy.grails.commons.ConfigurationHolder.config]}
		service.grailsApplication = service.getGrailsApplication()
	}
}

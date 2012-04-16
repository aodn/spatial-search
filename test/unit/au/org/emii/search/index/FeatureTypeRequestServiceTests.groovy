package au.org.emii.search.index

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
	
	def _applyConfig() {
		service.metaClass.getGrailsApplication = { -> [config: org.codehaus.groovy.grails.commons.ConfigurationHolder.config]}
		service.grailsApplication = service.getGrailsApplication()
	}
}

package au.org.emii.search.index.test.integration

import au.org.emii.search.index.GeonetworkMetadata;
import grails.test.*

class FeatureTypeRequestServiceTests extends GroovyTestCase {
    
	def featureTypeRequestService
	
	protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testGetFeatureTypeRequestImplementation() {
		def all = GeonetworkMetadata.findAllByFeatureTypeName('aims:AIMS_TRIP_5310')
		def metadata = all[0]
		
		def messages = []
		def impl = featureTypeRequestService._getFeatureTypeRequestImplementation(metadata, null, messages)
		
		assertTrue messages.isEmpty()
		assertTrue 'au.org.emii.search.index.NullFeatureTypeRequest' != impl.getClass().getName()
    }
	
	void testFetchOneAimsFeature() {
		def all = GeonetworkMetadata.findAllByFeatureTypeName('aims:AIMS_TRIP_5310')
		def metadata = all[0]
		assertFalse all.isEmpty()
		
		def messages = []
		def impl = featureTypeRequestService._getFeatureTypeRequestImplementation(metadata, null, messages)
		assertTrue messages.isEmpty()
		
		def features = new ArrayList(impl.requestFeatureType(metadata))
		assertFalse features.isEmpty()
	}
}

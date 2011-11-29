package au.org.emii.search.index.test.integration

import grails.test.*
import au.org.emii.search.index.FeatureTypeRequestClass

class FeatureTypeRequestClassTests extends GroovyTestCase {
    
	def grailsApplication
	
	protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testNullFeatureTypeCreation() {
		def clazz = new FeatureTypeRequestClass()
		clazz.className = 'au.org.emii.search.index.NullFeatureTypeRequest'
		
		clazz.grailsApplication = grailsApplication
		
		assertTrue clazz.getFeatureTypeRequest() != null
    }
}

package au.org.emii.search.test.unit

import org.codehaus.groovy.grails.commons.ConfigurationHolder;

import au.org.emii.search.GeoNetworkSearchShaBuilder;
import grails.test.*

class GeoNetworkSearchShaBuilderTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }
	
	void testCleanListParams() {
		def param = 'theme9,theme8,theme7,atheme,themez,btheme,theme1,theme5'
		def shaBuilder = new GeoNetworkSearchShaBuilder()
		
		mockConfig("geonetwork.search.list.params.delimiter = ','")
		_applyConfig(shaBuilder)
		
		def result = shaBuilder._cleanListParam(param)
		
		assertEquals("atheme,btheme,theme1,theme5,theme7,theme8,theme9,themez", result)
	}

    void testCleanParams() {
		def params = new TreeMap()
		params.from = '1'
		params.to = '15'
		params.fast = 'index'
		params['ext-comp-1'] = 'extcomponent'
		params['ext-comp-2'] = '1234567890'
		params['ext-comp-3'] = 'poipiwopiiwer'
		params['ext-comp-4'] = 'adasdasdsa'
		params['themekey'] = 'theme9,theme8,theme7,atheme,themez,btheme,theme1,theme5'
		
		def shaBuilder = new GeoNetworkSearchShaBuilder()
		
		def mockedConfig = new ConfigObject()
		mockedConfig.geonetwork.search.list.params.items = ['themekey', 'category', 'orgName', 'dataparam', 'longParamName']
		mockedConfig.geonetwork.search.list.params.delimiter = ','
		ConfigurationHolder.config = mockedConfig
		_applyConfig(shaBuilder)
		
		def result = shaBuilder._cleanParams(params)
		
		assertFalse(params.containsKey('from'))
		assertFalse(params.containsKey('to'))
		assertFalse(params.containsKey('fast'))
		assertFalse(params.containsKey('ext-comp-1'))
		assertFalse(params.containsKey('ext-comp-2'))
		assertFalse(params.containsKey('ext-comp-3'))
		assertFalse(params.containsKey('ext-comp-4'))
		assertEquals("atheme,btheme,theme1,theme5,theme7,theme8,theme9,themez", params.themekey)
    }
	
	def _applyConfig(o) {
		o.metaClass.getGrailsApplication = { -> [config: ConfigurationHolder.config]}
		o.grailsApplication = o.getGrailsApplication()
	}
}

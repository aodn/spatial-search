package au.org.emii.search

import au.org.emii.search.index.GeonetworkMetadata;

class SpatialSearchingTest extends GroovyTestCase {
	
	def geoNetworkRequestService
	def featureTypeRequestService
	def grailsApplication
	def static _queuedMetadata
	def static _indexed = false
	
	def testNothing() {}
	
	def _queue(refresh) {
		if (!_queuedMetadata || refresh) {
			_queuedMetadata = geoNetworkRequestService.queue([:])
		}
		return _queuedMetadata
	}
	
	def _index(refresh, featureTypeNames) {
		// Only index a subset of features when specified
		if (featureTypeNames) {
			def criteria = GeonetworkMetadata.createCriteria()
			def l = criteria.list {
				not {
					'in'('featureTypeName', featureTypeNames)
				}
			}
			l*.delete()
		}
		if (!_indexed || refresh || featureTypeNames) {
			featureTypeRequestService.index()
		}
	}
}

package au.org.emii.search.index

import java.sql.Timestamp

class IndexController {

	def grailsApplication
	def geoNetworkRequestService
	def featureTypeRequestService
	
    def queue = {
		render _queue(params)
	}
	
	def index = {
		render _index(params)
	}
	
	def harvest = {
		def message = _queue(params)
		message += _index(params)
		render message
	}
	
	def _queue(params) {
		def message = "Queueing started at ${new Date()}<br>"
		def metadata = geoNetworkRequestService.queue(params)
		message += "${metadata.size()} metadata documents queued finishing at ${new Date()}<br>"
		return message
	}
	
	def _index(params) {
		def message = "Indexing started at ${new Date()}<br>"
		def featureCount = featureTypeRequestService.index()
		message += "${featureCount} features indexed finishing at ${new Date()}<br>"
		return message
	}
}
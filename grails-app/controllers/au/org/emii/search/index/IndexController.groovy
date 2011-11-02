package au.org.emii.search.index

import java.sql.Timestamp

class IndexController {

	def grailsApplication
	def geoNetworkRequestService
	def featureTypeRequestService
	
    def queue = {
		def message = "Queueing started at ${new Date()}<br>"
		def documents = geoNetworkRequestService.queue(params)
		message += "${documents.size()} documents queued finishing at ${new Date()}<br>"
		render message
	}
	
	def index = {
		def message = "Indexing started at ${new Date()}<br>"
		def featureCount = featureTypeRequestService.index()
		message += "${featureCount} features indexed finishing at ${new Date()}<br>"
		render message
	}
}

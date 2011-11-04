package au.org.emii.search

class SearchController {

	def geoNetworkRequestService
	
    def search = {
		def features = geoNetworkRequestService.search(params)
		render "Found ${features.size()} features"
	}
}

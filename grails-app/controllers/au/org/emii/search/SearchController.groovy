package au.org.emii.search

class SearchController {

	def geoNetworkRequestService
	
    def index = {
		def xml = geoNetworkRequestService.search(params)
		render(text: xml, contentType: "text/xml", encoding:"UTF-8")
	}
}

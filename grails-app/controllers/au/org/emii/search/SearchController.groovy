
/*
 * Copyright 2012 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */
package au.org.emii.search

class SearchController {

	def geoNetworkRequestService
	
    def index = {
		def xml = geoNetworkRequestService.search(params)
		render(text: xml, contentType: "text/xml", encoding:"UTF-8")
	}
}

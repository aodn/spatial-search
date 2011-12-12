package au.org.emii.search

import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import au.org.emii.geoserver.GeoserverService;

class DescribeController {

    def index = { }
	
	// http://maps.aims.gov.au/geoserver/wfs?service=wfs&version=1.1.0&request=DescribeFeatureType&typeName=aims:WeatherStation
	
	//  Maps capabilities
	def query = {
		def url = _buildGetCapabilities(params.geoserver)
		def geoserver = new GeoserverService(_request(url))
		render(template: 'capabilities', model:[geoserver: geoserver])
	}
	
	def describe = {
		
	}
	
	def _request(url) {
		def httpResponse
		try {
			def request = new GetRequest()
			httpResponse = request.request(url, new BasicResponseHandler())
		}
		catch (Exception e) {
			log.error("", e)
			throw e
		}
		return httpResponse
	}
	
	def _buildBaseUrl(geoserver) {
		def url = geoserver
		// Check if there if the http:// has been specified
		if (!(url =~ /http:\/\//)) {
			url = "http://${url}"
		}
		return "${url}/geoserver/wfs?service=wfs&version=1.1.0"
	}
	
	def _buildGetCapabilities(geoserver) {
		return _buildBaseUrl(params.geoserver) + '&request=GetCapabilities'
	}
}

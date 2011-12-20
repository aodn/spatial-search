package au.org.emii.search

import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class GetRequest {

	static Logger log = LoggerFactory.getLogger(GetRequest.class)
	
	def contentTypeHeader
	def acceptHeader
	
	GetRequest() {
		contentTypeHeader = "text/xml"
		acceptHeader = "text/xml,application/xml;q=0.9"
	}
	
	def request(url, responseHandler) {
		def httpResponse
		try {
			def httpClient = new DefaultHttpClient()
			log.info("Requesting GET ${url}")
			def httpGet = _setupHttpGet(url)
			httpResponse = httpClient.execute(httpGet, responseHandler)
			httpClient.getConnectionManager().shutdown()
		}
		catch (Exception e) {
			log.error("", e)
			throw e
		}
		return httpResponse
	}
	
	def _setupHttpGet(url) {
		def httpGet = new HttpGet(url)
		httpGet.addHeader("Content-Type", contentTypeHeader)
		httpGet.addHeader("Accept", acceptHeader)
		return httpGet
	}
}

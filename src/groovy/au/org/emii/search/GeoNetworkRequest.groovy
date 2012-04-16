package au.org.emii.search

import org.apache.commons.io.IOUtils
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentProducer
import org.apache.http.entity.EntityTemplate
import org.apache.http.impl.client.BasicResponseHandler
import org.apache.http.impl.client.DefaultHttpClient
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

import freemarker.template.Configuration
import freemarker.template.DefaultObjectWrapper

class GeoNetworkRequest implements ApplicationContextAware {

	ApplicationContext applicationContext
	Configuration freeMarkerCfg
	
	def grailsApplication
	
	GeoNetworkRequest() {
		freeMarkerCfg = new Configuration()
		freeMarkerCfg.setObjectWrapper(new DefaultObjectWrapper())
	}
	
	def request(searchUrl, params) {
		_listifyKnownListParams(params)
		def contentProducer = _setupContentProducer(params)
		return _request(searchUrl, contentProducer)
	}
	
	def _setupContentProducer(params) {
		def resource = applicationContext.getResource(grailsApplication.config.geonetwork.request.template.file)
		def file = resource.file
		freeMarkerCfg.setDirectoryForTemplateLoading(file.parentFile)
		def template = freeMarkerCfg.getTemplate(resource.filename)
		
		def writeTo = {
			outstream ->
			
			def out
			try {
				out = new OutputStreamWriter(outstream, "UTF-8");
				template.process(params, out)
			}
			finally {
				IOUtils.closeQuietly(out)
				IOUtils.closeQuietly(outstream)
			}
		}
		return ['writeTo' : writeTo]
	}
	
	def _request(searchUrl, contentProducer) {
		def httpClient = new DefaultHttpClient()
		def httpPost = _setupHttpPost(searchUrl, contentProducer)
		def responseHandler = new BasicResponseHandler();
		def httpResponse = httpClient.execute(httpPost, responseHandler)
		httpClient.getConnectionManager().shutdown()
		return httpResponse
	}
	
	def _setupHttpPost(searchUrl, contentProducer) {
		def httpEntity = new EntityTemplate(contentProducer as ContentProducer)
		def httpPost = new HttpPost(searchUrl)
		httpPost.entity = httpEntity
		httpPost.addHeader("Content-Type", "text/xml")
		httpPost.addHeader("Accept", "text/xml,application/xml;q=0.9")
		return httpPost
	}
	
	def _listifyKnownListParams(params) {
		def knownListParams = grailsApplication.config.geonetwork.search.list.params.items
		params.each { name, value ->
			if (knownListParams.contains(name)) {
				params[name] = value.split(grailsApplication.config.geonetwork.search.list.params.delimiter)
			}
		}
	}
}

package au.org.emii.search

import java.sql.Timestamp;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import au.org.emii.search.index.IndexRun;
import au.org.emii.search.index.QueuedDocument;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;

class GeoNetworkRequestService implements ApplicationContextAware {

	ApplicationContext applicationContext
	Configuration freeMarkerCfg
	
	def grailsApplication
	
	static transactional = true
	
	GeoNetworkRequestService() {
		freeMarkerCfg = new Configuration()
		freeMarkerCfg.setObjectWrapper(new DefaultObjectWrapper())
	}
	
	def service(params) {
		def xml = request(params)
		return parseResponse(xml)
	}

    def request(params) {
		// Add the date params so we only fetch metadata records that have been
		// modified since last run
		def lastRun = getLastRun()
		params["dateFrom"] = lastRun
		params["dateTo"] = new Date()
		
		def contentProducer = setupContentProducer(params)
		return handleRequestResponse(contentProducer)
    }
	
	def parseResponse(xml) {
		def documents = parseXmlToQueuedDocuments(xml)
		def queuedDocuments = QueuedDocument.findAllByIndexRunIsNull()
		
		def added = new Timestamp(new Date().time)
		documents.removeAll(queuedDocuments)
		documents.each { document ->
			document.added = added
			document.save(failOnError: true)
		}
		
		return documents
	}
	
	def handleRequestResponse(contentProducer) {
		def url = grailsApplication.config.geonetwork.serverURL
		def httpClient = new DefaultHttpClient()
		def httpPost = setupHttpPost(url, contentProducer)
		return sendRequestAndGetResponse(httpClient, httpPost)
	}
	
	def sendRequestAndGetResponse(httpClient, httpMethod) {
		def responseHandler = new BasicResponseHandler();
		def httpResponse = httpClient.execute(httpMethod, responseHandler)
		httpClient.getConnectionManager().shutdown()
		return httpResponse
	}
	
	def setupHttpPost(url, contentProducer) {
		def httpEntity = new EntityTemplate(contentProducer as ContentProducer)
		def httpPost = new HttpPost(url)
		httpPost.entity = httpEntity
		httpPost.addHeader("Content-Type", "text/xml")
		httpPost.addHeader("Accept", "text/xml,application/xml;q=0.9")
		return httpPost
	}
	
	def setupContentProducer(params) {
		def resource = applicationContext.getResource(grailsApplication.config.geonetwork.request.template.file)
		def file = resource.file
		freeMarkerCfg.setDirectoryForTemplateLoading(file.parentFile)
		def template = freeMarkerCfg.getTemplate(resource.filename)
		
		def writeTo = {
			outstream ->
			def out = new OutputStreamWriter(outstream, "UTF-8");
			template.process(params, out)
			out.close()
			outstream.close()
		}
		return ['writeTo' : writeTo]
	}
	
	def getLastRun() {
		def c = IndexRun.createCriteria()
		def lastRun = c.get {
			projections {
				max("runDate")
			}
		}
		if (lastRun == null) {
			lastRun = new Timestamp(new Date(0).time)
		}
		log.debug(lastRun)
		return lastRun
	}
	
	def parseXmlToQueuedDocuments(xml) {
		def tree = new XmlSlurper().parseText(xml)
		def allRecords = tree.metadata
		log.debug(allRecords.size() + " metadata records returned")
		
		return parseMetadataElements(tree)
	}
	
	def parseMetadataElements(tree) {
		def documents = [] as Set
		tree.metadata.each { metadata ->
			def uuid = metadata.info.uuid.text()
			metadata.link.each { link ->
				def document = parseLinkElement(link)
				if (document) {
					document.geonetworkUuid = uuid
					documents << document
				}
			}
		}
		return documents
	}
	
	def parseLinkElement(link) {
		def document
		def protocol = grailsApplication.config.geonetwork.protocol
		def regexPattern = grailsApplication.config.geonetwork.feature.type.indentifier.regex
		if (link.@protocol.text() =~ protocol && link.@name.text() =~ /$regexPattern/) {
			document = new QueuedDocument()
			document.featureTypeName = link.@name.text()
			document.geoserverEndPoint = serverEndPointFrom(link.@href.text())
		}
		return document
	}
	
	def serverEndPointFrom(url) {
		return url.substring(0, url.lastIndexOf("/"))
	}
}

package au.org.emii.search.index

import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.BasicResponseHandler
import org.apache.http.impl.client.DefaultHttpClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import au.org.emii.search.FeatureType;
import au.org.emii.search.geometry.GeometryHelper;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKTReader;

class FeatureTypeRequest {

	static Logger log = LoggerFactory.getLogger(FeatureTypeRequest.class)
	
	def properties = []
	def featureTypeElementName
	def featureTypeIdElementName
	def featureTypeGeometryElementName
	def geometry
	def grailsApplication
	def geometryHelper
	
	FeatureTypeRequest() {
		super()
		geometryHelper = new GeometryHelper();
	}
	
	FeatureTypeRequest(String featureTypeElementName, String featureTypeIdElementName) {
		this(featureTypeElementName, featureTypeIdElementName, 'geometry')
	}
	
	FeatureTypeRequest(String featureTypeElementName, String featureTypeIdElementName, String featureTypeGeometryElementName) {
		this()
		this.featureTypeElementName = featureTypeElementName
		this.featureTypeIdElementName = featureTypeIdElementName
		this.featureTypeGeometryElementName = featureTypeGeometryElementName
		properties << this.featureTypeIdElementName
		properties << this.featureTypeGeometryElementName
	}
	
	String toGetUrl(GeonetworkMetadata metadata) {
		def url = "${metadata.geoserverEndPoint}/wfs?service=wfs&version=1.1.0&request=GetFeature&typeName=${metadata.featureTypeName}&propertyName=${properties.join(',')}"
		return url
	}
	
	def handleResponse(metadata, xml) {
		def features = [] as Set
		
		if (!xml) {
			return features
		}
		
		def tree = new XmlSlurper().parseText(xml)
		tree.featureMembers[0]."${featureTypeElementName}".each { featureTree ->
			def feature = new FeatureType(metadata)
			feature.featureTypeId = getFeatureId(featureTree)
			try { 
				feature.geometry = _toGeometry(featureTree."${featureTypeGeometryElementName}")
				features << feature
			}
			catch (Exception e) {
				log.error("Could not create geometry for feature ${feature}: " + e.getMessage())
			}
		}
		
		if (features.isEmpty()) {
			// We had some XML returned but it couldn't be turned into features
			// likely it's an error message of some sort so dump it to log for
			// now until we build up some sort of error parser thingy
			log.error(xml)
		}
		
		return features
	}
	
	def getFeatureId(featureTree) {
		return featureTree."${featureTypeIdElementName}".text()
	}
	
	def requestFeatureType(queuedDocument) {
		def httpResponse
		try {
			def httpClient = new DefaultHttpClient()
			def url = toGetUrl(queuedDocument)
			log.debug("Requesting WFS using GET ${url}")
			def httpGet = setupHttpGet(url)
			def responseHandler = getResponseHandler()
			httpResponse = httpClient.execute(httpGet, responseHandler)
			httpClient.getConnectionManager().shutdown()
		}
		catch (Exception e) {
			log.error("", e)
			throw e
		}
		return handleResponse(queuedDocument, httpResponse)
	}
	
	def getResponseHandler() {
		return new BasicResponseHandler();
	}
	
	def setupHttpGet(url) {
		def httpGet = new HttpGet(url)
		httpGet.addHeader("Content-Type", "text/xml")
		httpGet.addHeader("Accept", "text/xml,application/xml;q=0.9")
		return httpGet
	}
	
	def _toGeometry(geometryElement) {
		def geometryType = geometryElement.children()[0].name()
		def text = _getCoordinateText(geometryType, geometryElement)
		return geometryHelper.toGeometry(geometryType, text)
	}
	
	def _getCoordinateText(geometryType, geometryElement) {
		if ('curve' == geometryType.toLowerCase()) {
			return geometryElement.Curve.segments.LineStringSegment.join(', ')
		}
		return geometryElement.text()
	}
}
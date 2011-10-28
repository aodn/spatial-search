package au.org.emii.search.index

import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.BasicResponseHandler
import org.apache.http.impl.client.DefaultHttpClient
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import au.org.emii.search.FeatureType;

import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.io.WKTReader;

class FeatureTypeRequest {

	static Logger log = LoggerFactory.getLogger(FeatureTypeRequest.class)
	
	static final int SRID = 4326
	static final PrecisionModel PRECISION_MODEL = new PrecisionModel(PrecisionModel.FLOATING)
	
	def properties = []
	def featureTypeElementName
	def featureTypeIdElementName
	def featureTypeGeometryElementName
	def geometry
	def grailsApplication
	
	FeatureTypeRequest() {
		
	}
	
	FeatureTypeRequest(String featureTypeElementName, String featureTypeIdElementName) {
		this(featureTypeElementName, featureTypeIdElementName, 'geometry')
	}
	
	FeatureTypeRequest(String featureTypeElementName, String featureTypeIdElementName, String featureTypeGeometryElementName) {
		this.featureTypeElementName = featureTypeElementName
		this.featureTypeIdElementName = featureTypeIdElementName
		this.featureTypeGeometryElementName = featureTypeGeometryElementName
		properties << this.featureTypeIdElementName
		properties << this.featureTypeGeometryElementName
	}
	
	String toGetUrl(QueuedDocument queuedDocument) {
		def url = "${queuedDocument.geoserverEndPoint}/wfs?service=wfs&version=1.1.0&request=GetFeature&typeName=${queuedDocument.featureTypeName}&propertyName=${properties.join(',')}"
		return url
	}
	
	def handleResponse(queuedDocument, xml) {
		def features = [] as Set
		
		if (!xml) {
			return features
		}
		
		def tree = new XmlSlurper().parseText(xml)
		tree.featureMembers[0]."${featureTypeElementName}".each { featureTree ->
			def feature = new FeatureType(queuedDocument)
			feature.featureTypeId = getFeatureId(featureTree)
			try { 
				feature.geometry = toGeometry(featureTree."${featureTypeGeometryElementName}")
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
	
	def toGeometry(geometryElement) {
		def wkt = toWkt(geometryElement)
		def geom = new WKTReader(new GeometryFactory(PRECISION_MODEL, SRID)).read(wkt)
		return geom
	}
	
	def toWkt(geometryElement) {
		def geometryType = geometryElement.children()[0].name()
		try {
			return this."to$geometryType"(geometryElement)
		}
		catch (MissingMethodException mme) {
			log.error("Could not create geometry object likely due to unsupported shape: ${geometryType}: ${mme.getMessage()}")
		}
		catch (Exception e) {
			log.error("", e)
		}
	}
	
	def toLineString(geometryElement) {
		log.debug("Building LINESTRING")
		def coOrds = splitCoOrdsText(geometryElement.text())
		def builder = new StringBuilder(5000)
		builder.append('LINESTRING (')
		for (def i = 1; i < coOrds.size(); i += 2) {
			appendLongLatPair(builder, coOrds[i], coOrds[i - 1], ' ')
			appendPairDelimiter(builder)
		}
		removePairDelimiter(builder)
		builder.append(')')
		return builder.toString()
	}
	
	def toPoint(geometryElement) {
		log.debug("Building POINT")
		def coOrds = splitCoOrdsText(geometryElement.text())
		def builder = new StringBuilder(100)
		builder.append('POINT (')
		appendLongLatPair(builder, coOrds[1], coOrds[0], ' ')
		builder.append(')')
		return builder.toString()
	}
	
	/**
	 * This doesn't actually produce a Curve geometry, it concatenates all the
	 * segments into a single LineString.  After discussion in a meeting today
	 * (2011-10-24) it was deemed that curves weren't actually represented on 
	 * maps from geoserver
	 * 
	 * @param geometryElement
	 * @return
	 */
	def toCurve(geometryElement) {
		log.debug("Building CURVE")
		def builder = new StringBuilder(5000)
		builder.append('LINESTRING (')
		geometryElement.Curve.segments.LineStringSegment.each { segment ->
			def coOrds = splitCoOrdsText(segment.text())
			for (def i = 1; i < coOrds.size(); i += 2) {
				appendLongLatPair(builder, coOrds[i], coOrds[i - 1], ' ')
				appendPairDelimiter(builder)
			}
		}
		removePairDelimiter(builder)
		builder.append(')')
		return builder.toString()
	}
	
	def appendLongLatPair(builder, longitude, latitude, delimeter) {
		builder.append(longitude)
		builder.append(delimeter)
		builder.append(latitude)
	}
	
	def appendPairDelimiter(builder) {
		builder.append(', ')
	}
	
	def removePairDelimiter(builder) {
		builder.setLength(builder.length() - 2)
	}
	
	def splitCoOrdsText(coOrdsText) {
		return coOrdsText.split(' ')
	}
}
package au.org.emii.search.index

import groovy.xml.XmlUtil

import org.apache.http.impl.client.BasicResponseHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import au.org.emii.search.FeatureType
import au.org.emii.search.GetRequest
import au.org.emii.search.geometry.GeometryHelper

class FeatureTypeRequest {

	static Logger log = LoggerFactory.getLogger(FeatureTypeRequest.class)
	
	def properties = []
	def featureTypeElementName
	def featureTypeIdElementName
	def featureTypeGeometryElementName
	def featureMembersElementName
	def geometry
	def grailsApplication
	def geometryHelper
	def namespaceAware
	def outputFormat
	def idParser
	
	FeatureTypeRequest() {
		super()
		geometryHelper = new GeometryHelper()
		namespaceAware = true
		idParser = new FeatureTypeIndentifierParser()
	}
	
	FeatureTypeRequest(String featureTypeIdElementName) {
		this(featureTypeIdElementName, 'geometry')
	}
	
	FeatureTypeRequest(String featureTypeIdElementName, String featureTypeGeometryElementName) {
		this()
		this.featureTypeIdElementName = featureTypeIdElementName
		this.featureTypeGeometryElementName = featureTypeGeometryElementName
		// Don't push the id as a property if it's an attribute
		if (!this.featureTypeIdElementName.startsWith('@')) {
			properties << this.featureTypeIdElementName
		}
		properties << this.featureTypeGeometryElementName
	}
	
	FeatureTypeRequest(String featureTypeIdElementName, String featureTypeGeometryElementName, String namespaceAware) {
		this(featureTypeIdElementName, featureTypeGeometryElementName)
		try {
			this.namespaceAware = Boolean.valueOf(namespaceAware)
		}
		catch (Exception e) {
			log.error("", e)
		}
	}
	
	String toGetUrl(GeonetworkMetadata metadata) {
		// Describe feature info URL for convenience when adding new features
		// ${metadata.geoserverEndPoint}/wfs?service=wfs&version=1.1.0&request=DescribeFeatureType&typeName=topp:soop_ba_mv
		def getUrl = "${metadata.geoserverEndPoint}/wfs?service=wfs&version=1.1.0&request=GetFeature&srsName=EPSG:4326&typeName=${metadata.featureTypeName}&propertyName=${properties.join(',')}"
		if (_isGml2OutputDesired()) {
			getUrl += "&outputFormat=gml2"
		}
		return getUrl
	}
	
	def handleResponse(metadata, xml) {
		def features = [] as Set
		
		if (!xml) {
			return features
		}
		
		def tree = slurp(xml)
		tree."${featureMembersElementName}".each { featureMember ->
			featureMember.children().each { member ->
				if (featureTypeElementName == member.name()) {
					def feature = new FeatureType(metadata)
					feature.featureTypeId = _getFeatureId(member)
					try {
						feature.gml = getGml(member."${featureTypeGeometryElementName}")
						if (feature.gml) {
							features << feature
						}
					}
					catch (Exception e) {
						log.error("Could not create geometry for feature ${feature}: " + e.getMessage())
					}
				}
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
	
	def slurp(xml) {
		return new XmlSlurper(false, namespaceAware).parseText(xml)
	}
	
	def _getFeatureId(featureTree) {
		return idParser.parseFromNode(featureTypeIdElementName, featureTree)
	}
	
	def requestFeatureType(geonetworkMetadata) {
		featureTypeElementName = trimNamespace(geonetworkMetadata.featureTypeName)
		def httpResponse
		try {
			def request = new GetRequest()
			httpResponse = request.request(toGetUrl(geonetworkMetadata), getResponseHandler())
		}
		catch (Exception e) {
			log.error("", e)
			throw e
		}
		return handleResponse(geonetworkMetadata, httpResponse)
	}
	
	def getResponseHandler() {
		return new BasicResponseHandler();
	}
	
	def trimNamespace(name) {
		if (name.contains(':')) {
			return name.substring(name.lastIndexOf(':') + 1)
		}
		return name
	}
	
	def getGml(geometryElement) {
		if (geometryElement.isEmpty() || geometryElement.children().isEmpty()) {
			return null
		}
		return XmlUtil.serialize(geometryElement.children()[0])
	}
	
	def _isGml2OutputDesired() {
		return outputFormat && outputFormat == 'gml2'
	}
	
	def _setFeatureMembersElementName(featureMembersElementName) {
		if (featureMembersElementName) {
			this.featureMembersElementName = featureMembersElementName
		}
		else if (_isGml2OutputDesired()) {
			this.featureMembersElementName = 'featureMember'
		}
		else {
			this.featureMembersElementName = 'featureMembers'
		}
	}
	
	def configure(featureTypeRequestClass) {
		if (featureTypeRequestClass.outputFormat) {
			outputFormat = featureTypeRequestClass.outputFormat
		}
		_setFeatureMembersElementName(featureTypeRequestClass.featureMembersElementName)
	}
}
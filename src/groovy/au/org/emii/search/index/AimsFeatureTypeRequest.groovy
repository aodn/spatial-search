package au.org.emii.search.index

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.emii.search.FeatureType;

class AimsFeatureTypeRequest extends FeatureTypeRequest {

	static Logger log = LoggerFactory.getLogger(AimsFeatureTypeRequest.class)
	
	AimsFeatureTypeRequest() {
		super()
	}
	
	AimsFeatureTypeRequest(String featureTypeIdElementName, String featureTypeGeometryElementName) {
		super(featureTypeIdElementName, featureTypeGeometryElementName)
	}
	
	AimsFeatureTypeRequest(String featureTypeIdElementName) {
		super(featureTypeIdElementName)
	}
	
	String toGetUrl(GeonetworkMetadata metadata) {
		return super.toGetUrl(metadata) + '&outputFormat=gml2'
	}
	
	def handleResponse(metadata, xml) {
		def features = [] as Set
		
		if (!xml) {
			return features
		}
		
		featureTypeElementName = trimNamespace(metadata.featureTypeName)
		def tree = new XmlSlurper(false, true).parseText(xml)
		tree.featureMember.each { featureMember ->
			def feature = new FeatureType(metadata)
			featureMember.children().each { member ->
				if (featureTypeElementName == member.name()) {
					feature.featureTypeId = getFeatureId(member)
					try {
						feature.geometry = _toGeometry(member."${featureTypeGeometryElementName}")
						features << feature
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
}

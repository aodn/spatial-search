package au.org.emii.search.index

class Gml2FeatureTypeRequest extends FeatureTypeRequest {

	Gml2FeatureTypeRequest() {
		super()
	}
	
	Gml2FeatureTypeRequest(String featureTypeIdElementName, String featureTypeGeometryElementName) {
		super(featureTypeIdElementName, featureTypeGeometryElementName)
	}
	
	Gml2FeatureTypeRequest(String featureTypeIdElementName) {
		super(featureTypeIdElementName)
	}
	
	Gml2FeatureTypeRequest(String featureTypeIdElementName, String featureTypeGeometryElementName, String namespaceAware) {
		super(featureTypeIdElementName, featureTypeGeometryElementName, namespaceAware)
	}
	
	@Override
	String toGetUrl(GeonetworkMetadata metadata) {
		return super.toGetUrl(metadata) + "&outputFormat=gml2"
	}
	
	def init() {
		// Post construction hook
		featureMembersElementName = 'featureMember'
	}
}

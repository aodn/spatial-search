package au.org.emii.search.index

class IdAsAttributeFeatureTypeRequest extends FeatureTypeRequest {
	
	/*
	 * Some features use the gml:id attribute as the id, this is the default
	 * implementation for those features
	 */
	
	def idAttributeName
	
	IdAsAttributeFeatureTypeRequest(String featureTypeGeometryElementName) {
		this(featureTypeGeometryElementName, 'id')
	}
	
	IdAsAttributeFeatureTypeRequest(String featureTypeGeometryElementName, String idAttributeName) {
		super()
		this.idAttributeName = idAttributeName
		this.featureTypeGeometryElementName = featureTypeGeometryElementName
		properties << this.featureTypeGeometryElementName
	}
	
	def getFeatureId(featureTree) {
		return featureTree.@"$idAttributeName".text()
	}
}

package au.org.emii.search.index

class AutonomousUnderwaterVehicleRequest extends FeatureTypeRequest {

	/*
	 * AUVs don't provide us with a consistent id so we have to use the gml:id
	 * attribute but this isn't guaranteed to be unique in future versions of
	 * geoserver, it's only unique per document. To date the only feature that
	 * hasn't been consistent in mapping gml:id to a record has been 
	 * topp:soop_asf but at this stage I prefer the usage of gml:id to be the
	 * exception rather than the rule at the moment. It's a small change to
	 * revert other features to use the gml:id but some additional code will
	 * be required to make topp:soop_asf (and potentially other soop features)
	 * have a unique id
	 */
	
	AutonomousUnderwaterVehicleRequest(String featureTypeGeometryElementName) {
		super()
		this.featureTypeGeometryElementName = featureTypeGeometryElementName
		properties << this.featureTypeGeometryElementName
	}
	
	def getFeatureId(featureTree) {
		return featureTree.@id.text()
	}
}

package au.org.emii.search

import org.apache.commons.lang.builder.ToStringBuilder;

import com.vividsolutions.jts.geom.Geometry;

class FeatureType {

	String geonetworkUuid
	String featureTypeName
	String featureTypeId
	Geometry geometry
	String gml
	
	static mapping = {
		featureTypeName index: 'idx_ft_feature_type'
		geonetworkUuid index: 'idx_ft_feature_type'
		featureTypeId index: 'idx_ft_feature_type'
		geometry index: 'idx_ft_geometry'
	}
	
    static constraints = {
    }
	
	static transients = ['gml']
	
	FeatureType() {
		super()
	}
	
	FeatureType(metadata) {
		this()
		geonetworkUuid = metadata.geonetworkUuid
		featureTypeName = metadata.featureTypeName
	}
	
	@Override
	String toString() {
		return new ToStringBuilder(this)
			.append("geonetworkUuid", geonetworkUuid)
			.append("featureTypeName", featureTypeName)
			.append("featureTypeId", featureTypeId)
			.append("geometry", geometry?.geometryType)
			.toString()
	}
}


/*
 * Copyright 2012 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */
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
			.append("gml", gml)
			.toString()
	}
	
	def getSridFromGml() {
		def gmlSrid
		if (gml) {
			def geomGml = new XmlParser(false, false).parseText(gml)
			if (_srsUsesUrn(geomGml.@srsName)) {
				gmlSrid = _getSridFromGmlUrn(geomGml.@srsName)
			}
			else {
				gmlSrid = _getSridFromGmlHttp(geomGml.@srsName)
			}
			
		}
		// Default to the most common case on failure on the chance it will probably work for us
		return gmlSrid ?: "4326"
	}
	
	def _srsUsesUrn(srsName) {
		return srsName.startsWith("urn")
	}
	
	def _getSridFromGmlUrn(srsName) {
		return _getSridFromGmlUsingSplitChar(srsName, ":")
	}
	
	def _getSridFromGmlHttp(srsName) {
		return _getSridFromGmlUsingSplitChar(srsName, "#")
	}
	
	def _getSridFromGmlUsingSplitChar(srsName, splitter) {
		def pieces = srsName.split(splitter)
		if (pieces.size() > 1) {
			return pieces[pieces.size() - 1]
		}
	}
}

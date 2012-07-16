package au.org.emii.search.index

import java.sql.Timestamp

import org.apache.commons.lang.builder.CompareToBuilder
import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder
import org.apache.commons.lang.builder.ToStringBuilder

import com.vividsolutions.jts.geom.Geometry

class GeonetworkMetadata implements Comparable<GeonetworkMetadata> {

	String geonetworkUuid
	Timestamp added
	String featureTypeName
	String geoserverEndPoint
	Timestamp changeDate
	Timestamp lastIndexed
	Geometry geoBox

	def error

	static mapping = {
		featureTypeName index: 'idx_qd_feature_type'
		geonetworkUuid index: 'idx_qd_feature_type'
	}

	static constraints = {
		added(nullable : true)
		changeDate(nullable : false)
		lastIndexed(nullable : true)
		geoBox(nullable : true)
	}

	@Override
	boolean equals(o) {
		if (is(o)) {
			return true
		}
		if (!(o instanceof GeonetworkMetadata)) {
			return false
		}

		return new EqualsBuilder()
			.append(geonetworkUuid, o.geonetworkUuid)
			.append(featureTypeName, o.featureTypeName)
			.isEquals()
	}

	@Override
	int hashCode() {
		return new HashCodeBuilder()
			.append(geonetworkUuid)
			.append(featureTypeName)
			.toHashCode()
	}

	@Override
	String toString() {
		return new ToStringBuilder(this)
			.appendToString(idString())
			.append("geoserverEndPoint", geoserverEndPoint)
			.append("added", added)
			.append("changeDate", changeDate)
			.append("lastIndexed", lastIndexed)
			.toString()
	}

	int compareTo(o) {
		return new CompareToBuilder()
			.append(geonetworkUuid, o.geonetworkUuid)
			.append(featureTypeName, o.featureTypeName)
			.toComparison()
	}

	String idString() {
		return new ToStringBuilder(this)
			.append("geonetworkUuid", geonetworkUuid)
			.append("featureTypeName", featureTypeName)
			.toString()
	}

	def unindexed() {
		return lastIndexed == null || changeDate.after(lastIndexed)
	}
}

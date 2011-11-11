package au.org.emii.search.index

import java.sql.Timestamp

import org.apache.commons.lang.builder.CompareToBuilder;
import org.apache.commons.lang.builder.EqualsBuilder
import org.apache.commons.lang.builder.HashCodeBuilder
import org.apache.commons.lang.builder.ToStringBuilder

class GeonetworkMetadata implements Comparable<GeonetworkMetadata> {

	String geonetworkUuid
	Timestamp added
	String featureTypeName
	String geoserverEndPoint
	
	def keywordCounts = [:]

	static mapping = {
		featureTypeName index: 'idx_qd_feature_type'
		geonetworkUuid index: 'idx_qd_feature_type'
		indexRun indexColumn:[name:"idx_qd_index_run_id", type:Integer]
	}
	
	static constraints = {
		added(nullable : true)
		indexRun(nullable : true)
	}
	
	static belongsTo = [
		indexRun: IndexRun
	]
	
	def addKeyword(keyword) {
		def keywordCount = keywordCounts[keyword]
		if (!keywordCount) {
			keywordCount = 0
		}
		keywordCounts[keyword] = keywordCount + 1
	}
	
	def subtractKeyword(keyword) {
		def keywordCount = keywordCounts[keyword]
		if (keywordCount) {
			keywordCounts[keyword] = keywordCount - 1
		}
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
			.append("geonetworkUuid", geonetworkUuid)
			.append("featureTypeName", featureTypeName)
			.append("geoserverEndPoint", geoserverEndPoint)
			.append("added", added)
			.append("index", indexRun?.id)
			.toString()
	}
	
	int compareTo(o) {
		return new CompareToBuilder()
			.append(geonetworkUuid, o.geonetworkUuid)
			.append(featureTypeName, o.featureTypeName)
			.toComparison()
	}
}

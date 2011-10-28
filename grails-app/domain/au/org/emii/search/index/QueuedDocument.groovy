package au.org.emii.search.index

import java.sql.Timestamp

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

class QueuedDocument {

	String geonetworkUuid
	Timestamp added
	String featureTypeName
	String geoserverEndPoint

	static belongsTo = [
		indexRun: IndexRun
	]
		
    static constraints = {
		added(nullable : true)
		indexRun(nullable : true)
    }
	
	static mapping = {
		featureTypeName index: 'idx_qd_feature_type'
		geonetworkUuid index: 'idx_qd_feature_type'
		indexRun indexColumn:[name:"idx_qd_index_run_id", type:Integer]
	}
	
	@Override
	boolean equals(o) {
		if (is(o)) {
			return true;
		}
		if (!(o instanceof QueuedDocument)) {
			return false;
		}
		
		return new EqualsBuilder()
			.append(geonetworkUuid, o.geonetworkUuid)
			.isEquals()
	}
	
	@Override
	int hashCode() {
		return new HashCodeBuilder()
			.append(geonetworkUuid)
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
}

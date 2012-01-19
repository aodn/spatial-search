package au.org.emii.search.index

import java.sql.Timestamp;

import org.apache.commons.lang.builder.ToStringBuilder;

class IndexRun {

	Timestamp runDate
	
    static constraints = {
    }
	
	static hasMany = [
		geonetworkMetadataDocs: GeonetworkMetadata	
	]
	
	IndexRun() {
		super()
		runDate = new Timestamp(System.currentTimeMillis())
		geonetworkMetadataDocs = []
	}
	
	@Override
	String toString() {
		return new ToStringBuilder(this)
			.append("runDate", runDate)
			.toString()
	}
}

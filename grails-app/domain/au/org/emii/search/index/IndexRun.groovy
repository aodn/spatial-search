package au.org.emii.search.index

import java.sql.Timestamp;

import org.apache.commons.lang.builder.ToStringBuilder;

class IndexRun {

	Timestamp runDate
	Integer documents
	Integer failures
	
    static constraints = {
		documents(nullable: true)
		failures(nullable: true)
    }
	
	static hasMany = [
		geonetworkMetadataDocs: GeonetworkMetadata	
	]
	
	IndexRun() {
		super()
		runDate = new Timestamp(System.currentTimeMillis())
		documents = new Integer(0)
		failures = new Integer(0)
		geonetworkMetadataDocs = []
	}
	
	@Override
	String toString() {
		return new ToStringBuilder(this)
			.append("runDate", runDate)
			.append("documents", documents)
			.append("failures", failures)
			.toString()
	}
}

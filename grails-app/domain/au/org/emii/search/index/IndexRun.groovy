package au.org.emii.search.index

import java.sql.Timestamp;

class IndexRun {

	Timestamp runDate
	Integer documents
	Integer failures
	
    static constraints = {
		documents(nullable: true)
		failures(nullable: true)
    }
	
	static hasMany = [
		queuedDocuments: QueuedDocument	
	]
	
	IndexRun() {
		super()
		runDate = new Timestamp(System.currentTimeMillis())
		documents = new Integer(0)
		failures = new Integer(0)
	}
}

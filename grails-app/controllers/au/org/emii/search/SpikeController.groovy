package au.org.emii.search

class SpikeController {
	
	def grailsApplication

    def index = {
		/*
		def index = FSDirectory.open(new File("/opt/geonetwork/web/geonetwork/WEB-INF/lucene/nonspatial"))
		
		// Lucene query from Luke _changeDate:[2011\-01\-01T00:00:00 TO NOW]
		// OGC:WMS-1.1.1-http-get-map
		
		def searcher = new IndexSearcher(index, true)
		
		//def analyzer = new StandardAnalyzer(Version.LUCENE_30)
		def analyzer = new KeywordAnalyzer()
		//def analyzer = new GeoNetworkAnalyzerMirror()
		def query = new QueryParser(Version.LUCENE_30, 'protocol', analyzer).parse('OGC\\:WMS-1.1.1-http-get-map')
		def collector = new AllMatchCollector()
		
		searcher.search(query, collector)
		
		def docs = collector.getDocuments()
		log.debug(docs.size())
		//docs.each { doc ->
		//	log.debug(doc)
		//}
		
		def doc = new ArrayList<Document>(docs)[0]
		def fields = doc.fields
		fields.each { field ->
			log.debug(field.name() + " => " + doc.getValues(field.name()))	
		}
		*/
		render "done"
	}
}

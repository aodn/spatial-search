package au.org.emii.search

import groovy.xml.MarkupBuilder;

import java.io.Serializable;

import org.slf4j.Logger
import org.slf4j.LoggerFactory;

class GeoNetworkKeywordSummary implements Serializable {
	
	static Logger log = LoggerFactory.getLogger(GeoNetworkKeywordSummary.class)

	def keywordsMap
	def hitsUsedForSummary
	def recordCounter
	def pages
	
	GeoNetworkKeywordSummary() {
		keywordsMap = [:]
		hitsUsedForSummary = 0
		recordCounter = 0
		pages = []
	}
	
	def addNodeKeywords(metadataNode) {
		metadataNode.keyword.each { keywordElement ->
			def t = keywordElement.text()
			def keyword = keywordsMap[t]
			if (!keyword) {
				keyword = new GeoNetworkKeyword(name: t, indexKey: 'keyword')
				_addKeyword(keyword)
			}
			keyword.increment()
		}
		hitsUsedForSummary++
	}
	
	def buildSummaryXmlNode(builder) {
		def keywordSummariesToDisplay = getKeywords()
		if (keywordSummariesToDisplay?.size() > 10) {
			keywordSummariesToDisplay = keywordSummariesToDisplay[0..9]
		}
		builder.summary(count: hitsUsedForSummary, type: 'local', hitsusedforsummary: hitsUsedForSummary) {
			keywordSummariesToDisplay.each { keyword ->
				_buildSummaryKeywordNode(builder, keyword)
			}
		}
	}
	
	def updatePageParams(params) {
		try {
			def pageSize = params.to.toInteger() - params.from.toInteger() + 1
			def pageNumber = ((int)params.to.toInteger() / pageSize) - 1
			def page = pages[pageNumber]
			log.debug("Calculated page size is $pageSize using ${params.from} and ${params.to}")
			log.debug("Calculated page number is $pageNumber")
			log.debug("Returning page ${page.toString()}")
			
			params.from = page.from.toString()
			params.to = page.to.toString()
		}
		catch (ArrayIndexOutOfBoundsException e) {
			log.error("Not enough pages parsed for params ${params}:\n", e)
		}
		catch (Exception e) {
			log.error("", e)
		}
	}
	
	def _buildSummaryKeywordNode(builder, keyword) {
		builder.keyword(count: keyword.count, name: keyword.name, indexKey: keyword.indexKey)
	}
	
	def page(pageSize) {
		recordCounter++
		if (hitsUsedForSummary % pageSize == 0) {
			_page()
		}
	}
	
	def pageFastForward(pageSize) {
		recordCounter += pageSize
	}
	
	def close() {
		_page()
	}
	
	def _page() {
		pages << new GeoNetworkSpatialSearchPage(_getPageNumberForCurrentPage(), _getFromValueForCurrentPage(), recordCounter)
	}
	
	def _addKeyword(keyword) {
		keywordsMap[keyword.name] = keyword
	}
	
	def getKeywords() {
		return _sort()
	}
	
	def _sort() {
		def l = []
		l.addAll(keywordsMap.values())
		l = l.sort({ a, b -> 
				a.compareTo(b) * (-1) 
			}
		)
		return l
	}
	
	def _getFromValueForCurrentPage() {
		if (!pages.isEmpty()) {
			def lastPage = pages[pages.size() - 1]
			return lastPage.to + 1
		}
		return 1
	}
	
	def _getPageNumberForCurrentPage() {
		return pages.size() + 1
	}
	
	@Override
	String toString() {
		def writer = new StringWriter()
		def builder = new MarkupBuilder(writer)
		buildSummaryXmlNode(builder)
		return writer.toString()
	}
	
	class GeoNetworkSpatialSearchPage implements Serializable {
		def pageNumber
		def from
		def to
		
		GeoNetworkSpatialSearchPage(pageNumber, from, to) {
			this.pageNumber = pageNumber
			this.from = from
			this.to = to
		}
		
		@Override
		String toString() {
			return "Page $pageNumber is from $from to $to"
		}
	}
}

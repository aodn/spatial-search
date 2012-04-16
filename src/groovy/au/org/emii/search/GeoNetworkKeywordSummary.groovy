package au.org.emii.search

import org.slf4j.Logger
import org.slf4j.LoggerFactory;

class GeoNetworkKeywordSummary {
	
	static Logger log = LoggerFactory.getLogger(GeoNetworkKeywordSummary.class)

	def keywordsMap
	def hitsUsedForSummary
	def to
	
	GeoNetworkKeywordSummary() {
		keywordsMap = [:]
		hitsUsedForSummary = 0
		to = 0
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
	
	def merge(otherSummary) {
		otherSummary.getKeywords().each { otherKeyword ->
			def keyword = keywordsMap[otherKeyword.name]
			_mergeKeywords(keyword, otherKeyword)
		}
		hitsUsedForSummary += otherSummary.hitsUsedForSummary
		to = to >= otherSummary.to ? to : otherSummary.to
	}
	
	def _mergeKeywords(keyword, other) {
		if (!keyword) {
			_addKeyword(other)
		}
		else {
			keyword.count += other.count
		}
	}
	
	def _addKeyword(keyword) {
		keywordsMap[keyword.name] = keyword
	}
	
	def getKeywords() {
		return _sort()
	}
	
	def _sort() {
		final l = []
		l.addAll(keywordsMap.values())
		Collections.sort(l, Collections.reverseOrder())
		return l
	}
}


/*
 * Copyright 2012 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */
package au.org.emii.search

import groovy.xml.MarkupBuilder;

import java.io.Serializable;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger
import org.slf4j.LoggerFactory;

class GeoNetworkKeywordSummary implements Serializable {

    static Logger log = LoggerFactory.getLogger(GeoNetworkKeywordSummary.class)

    def grailsApplication
    def keywordsMap
    def hitsUsedForSummary
    def hitsUsedForCurrentPage
    def recordCounter
    def pages

    def keywordsInSummary

    GeoNetworkKeywordSummary(grailsApplication) {
        keywordsMap = [:]
        hitsUsedForSummary = 0
        hitsUsedForCurrentPage = 0
        recordCounter = 0
        pages = []
        this.grailsApplication = grailsApplication
        keywordsInSummary = _getGrailsApplication().config.geonetwork.keywords
    }

    def addNodeKeywords(metadataNode) {
        keywordsInSummary.each { keywordAttribute ->
            metadataNode."${keywordAttribute.key}".each { keywordElement ->
                def t = keywordElement.text()
                def keyword = keywordsMap[t]
                if (!keyword) {
                    keyword = new GeoNetworkKeyword(name: t, indexKey: keywordAttribute.value)
                    _addKeyword(keyword)
                }
                keyword.increment()
            }

            hitsUsedForSummary++
            hitsUsedForCurrentPage++
        }
    }

    def buildSummaryXmlNode(builder) {
        def keywordSummariesToDisplay = getKeywords()
        if (keywordSummariesToDisplay?.size() > 10) {
            keywordSummariesToDisplay = keywordSummariesToDisplay[0..9]
        }

        // Builds XML as it should be returned from GeoNetwork
        builder.summary(count: hitsUsedForSummary, type: 'local', hitsusedforsummary: hitsUsedForSummary) {
            keywordsInSummary.each { keywordAttribute ->
                buildTreeForKeyword(builder, keywordAttribute.value, keywordSummariesToDisplay)
            }
        }
    }

    def buildTreeForKeyword(builder, keyword, keywordSummariesToDisplay) {
        builder."${keyword}s" {
            keywordSummariesToDisplay.each { keywordElement ->
                if (keywordElement.indexKey == keyword) {
                    "${keywordElement.indexKey}"(count: keywordElement.count, name: keywordElement.name, indexKey: keywordElement.indexKey)
                }
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

    def page(pageSize) {
        recordCounter++
        if (hitsUsedForCurrentPage > 0 && hitsUsedForCurrentPage % pageSize == 0) {
            _page()
        }
    }

    def pageFastForward(pageSize) {
        recordCounter += pageSize
    }

    def getKeywords() {
        return _sort()
    }

    def isLastPage(geoNetworkResponse) {
        def lastPage = _getLastPage()
        if (lastPage) {
            return geoNetworkResponse.to >= lastPage.to
        }
        return false
    }

    def close(pageSize) {
        if (hitsUsedForCurrentPage % pageSize > 0) {
            _page()
        }
    }

    @Override
    String toString() {
        def writer = new StringWriter()
        def builder = new MarkupBuilder(writer)
        buildSummaryXmlNode(builder)
        return writer.toString()
    }

    def _page() {
        def page = new GeoNetworkSpatialSearchPage(_getPageNumberForCurrentPage(), _getFromValueForCurrentPage(), recordCounter)
        pages << page
        log.debug(page.toString())
        hitsUsedForCurrentPage = 0
    }

    def _addKeyword(keyword) {
        keywordsMap[keyword.name] = keyword
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

    def _getLastPage() {
        if (_hasPages()) {
            return pages[pages.size() - 1]
        }

        return null
    }

    def _hasPages() {
        return CollectionUtils.isNotEmpty(pages)
    }

    def _getGrailsApplication() {
        return grailsApplication
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

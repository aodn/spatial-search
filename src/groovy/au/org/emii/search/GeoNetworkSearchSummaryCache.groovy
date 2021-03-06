/*
 * Copyright 2012 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */
package au.org.emii.search

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import net.sf.ehcache.CacheManager
import net.sf.ehcache.Element

class GeoNetworkSearchSummaryCache {

    static final Logger log = LoggerFactory.getLogger(GeoNetworkSearchSummaryCache.class)

    final def CACHE_NAME = "geoNetworkKeywordSummaryCache"

    def manager

    GeoNetworkSearchSummaryCache() {
        manager = CacheManager.create()
        if (!manager.cacheExists(CACHE_NAME)) {
            manager.addCache(CACHE_NAME)
            _setDynamicConfigurationParameters()
        }
    }

    def add(sha, summary) {
        _getCache().put(new Element(sha, summary));
    }

    def get(sha) {
        def summary
        def element = _getCache().get(sha)
        if (element) {
            summary = element.getValue()
        }
        return summary
    }

    def clear() {
        _getCache().removeAll()
    }

    def _getCache() {
        return manager.getCache(CACHE_NAME)
    }

    def _setDynamicConfigurationParameters() {
        def configuration = _getCache().getCacheConfiguration()
        // Live forever
        configuration.setTimeToLiveSeconds(0)
        // Unless idle for a day
        configuration.setTimeToIdleSeconds(86400)
    }
}

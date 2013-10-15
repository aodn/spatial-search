
/*
 * Copyright 2012 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */
package au.org.emii.search.index

class IndexController {

    def geoNetworkRequestService
    def featureTypeRequestService

    def queue = {
        render _queue(params)
    }

    def index = {
        render _index(params)
    }

    def harvest = {
        def message = _queue(params)
        message += _index(params)
        render message
    }

    def _queue(params) {
        def message = "Queueing started at ${new Date()}<br>"
        def metadata = geoNetworkRequestService.queue(params)
        message += "${metadata.size()} metadata documents queued finishing at ${new Date()}<br>"
        return message
    }

    def _index(params) {
        def message = "Indexing started at ${new Date()}<br>"
        runAsync {
            featureTypeRequestService.index()
        }
        return message
    }
}

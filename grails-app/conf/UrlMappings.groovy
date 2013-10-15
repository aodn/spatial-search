
/*
 * Copyright 2012 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */
class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }

        "/" {
            controller = "search"
            action = "index"
            from = '1'
            // to parameter seems to be getting ignored at the moment
            to = '15'
            fast = 'false'
            // Bit yucky but will do until I research a neat way of injecting the
            // GrailsApplication into here
            protocol = 'OGC:WMS-1.1.1-http-get-map or OGC:WMS-1.3.0-http-get-map'
        }
        "500"(view:'/error')
    }
}

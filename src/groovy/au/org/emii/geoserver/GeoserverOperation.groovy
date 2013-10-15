
/*
 * Copyright 2012 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */
package au.org.emii.geoserver

class GeoserverOperation {

    def name
    def getLink
    def postLink
    def parameters

    GeoserverOperation() {
        parameters = [:]
    }

    GeoserverOperation(node) {
        this()
        name = node.@name.text()
        getLink = node.DCP.HTTP.Get.@href.text()
        postLink = node.DCP.HTTP.Post.@href.text()
        _readParams(node)
    }

    def _readParams(node) {
        node.Parameter.each { parameter ->
            parameters[parameter.@name.text()] = _readParamValues(parameter)
        }
    }

    def _readParamValues(parameter) {
        def values = []
        parameter.Value.each { value ->
            values << value.text()
        }
        return values
    }
}

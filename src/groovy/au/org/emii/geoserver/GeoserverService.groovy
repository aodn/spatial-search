
/*
 * Copyright 2012 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */
package au.org.emii.geoserver

class GeoserverService {

    def provider
    def individual
    def position
    def operations

    GeoserverService(xml) {
        def slurp = new XmlSlurper().parseText(xml)
        provider = slurp.ServiceProvider.ProviderName.text()
        individual = slurp.ServiceProvider.ServiceContact.IndividualName.text()
        position = slurp.ServiceProvider.ServiceContact.PositionName.text()
        _readOperations(slurp)
    }

    def _readOperations(node) {
        operations = []
        node.OperationsMetadata.Operation.each { operationNode ->
            operations << new GeoserverOperation(operationNode)
        }
    }
}

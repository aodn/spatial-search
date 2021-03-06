
/*
 * Copyright 2012 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */
package au.org.emii.search.index

class FeatureTypeIndentifierParser {

    def parseFromNode(featureTypeIdElementName, node) {
        return node."${featureTypeIdElementName}".text()
    }

    def parseInlineAsCharacters(featureTypeIdElementName, chars, offset, length) {
        if (!_idIsAttribute(featureTypeIdElementName)) {
            return new String(chars, offset, length)
        }
    }

    def parseInlineAsAttribute(featureTypeIdElementName, ns, localName, qname, atts) {
        if (_idIsAttribute(featureTypeIdElementName)) {
            def attName = _getIdAttributeName(featureTypeIdElementName)
            return atts.getValue(attName)
        }
    }

    def _idIsAttribute(featureTypeIdElementName) {
        return featureTypeIdElementName.startsWith("@")
    }

    def _getIdAttributeName(featureTypeIdElementName) {
        return featureTypeIdElementName.substring(1)
    }
}

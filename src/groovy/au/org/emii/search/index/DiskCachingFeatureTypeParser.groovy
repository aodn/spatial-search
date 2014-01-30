
/*
 * Copyright 2012 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */
package au.org.emii.search.index

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.xml.sax.Attributes
import org.xml.sax.ext.DefaultHandler2

import au.org.emii.search.FeatureType

class DiskCachingFeatureTypeParser extends DefaultHandler2 {

    static final Logger log = LoggerFactory.getLogger(DiskCachingFeatureTypeParser.class)

    def metadata
    def featureTypeElementName
    def featureTypeIdElementName
    def featureTypeGeometryElementName
    def featureCallback
    def metadataCallback
    def idParser

    // State/flow controls
    def featureType
    def featureTypes
    def featureTypeGmlBuilder
    def featureCollectionStarted
    def charactersHandler

    DiskCachingFeatureTypeParser(GeonetworkMetadata metadata, DiskCachingFeatureTypeRequest featureTypeRequest) {
        featureTypes = []
        featureCollectionStarted = false
        idParser = new FeatureTypeIndentifierParser()

        this.metadata = metadata
        this.featureTypeElementName = featureTypeRequest.featureTypeElementName
        this.featureTypeIdElementName = featureTypeRequest.featureTypeIdElementName
        this.featureTypeGeometryElementName = featureTypeRequest.featureTypeGeometryElementName
        this.featureCallback = featureTypeRequest.featureCallback
        this.metadataCallback = featureTypeRequest.metadataCallback
    }

    void startElement(String ns, String localName, String qname, Attributes atts) {
        if (qname =~ /$featureTypeElementName/) {
            _startFeatureType(ns, localName, qname, atts)
        }
        if (qname =~ /$featureTypeIdElementName/) {
            charactersHandler = _parseFeatureTypeId
        }
        if (qname =~ /$featureTypeGeometryElementName/) {
            charactersHandler = _parseGeometryElement
        }
        if (qname =~ /gml:([^featureMember]|[^boundedBy]|[^null])/) {
            _printGmlStartTag(qname, atts)
        }
    }

    void endElement(String ns, String localName, String qname) {
        if (qname =~ /$featureTypeElementName/) {
            charactersHandler = null
            _endFeatureType(ns, localName, qname)
        }
        if (qname =~ /$featureTypeGeometryElementName/) {
            charactersHandler = null
            _endGeometryElement(ns, localName, qname)
        }
        if (qname =~ /gml:([^featureMember]|[^boundedBy]|[^null])/) {
            _printGmlEndTag(qname)
        }
    }

    void characters(char[] chars, int offset, int length) {
        if (charactersHandler) {
            charactersHandler(chars, offset, length)
        }
    }

    void endDocument() {
        if (metadataCallback) {
            metadataCallback(this.metadata)
        }
    }

    def _startFeatureType(ns, localName, qname, atts) {
        featureType = new FeatureType(metadata)
        featureType.featureTypeId = featureType.featureTypeId ?: idParser.parseInlineAsAttribute(featureTypeIdElementName, ns, localName, qname, atts)
        featureTypeGmlBuilder = new StringBuilder()
    }

    def _printGmlStartTag(qname, atts) {
        if (!featureType) {
            return
        }

        featureTypeGmlBuilder.append("<").append(_stripGmlNamespaceFromElements(qname))
        for (def i = 0; i < atts.getLength(); i++) {
            featureTypeGmlBuilder
                .append(" ")
                .append(atts.getLocalName(i))
                .append("=\"")
                .append(atts.getValue(i))
                .append("\"")
        }
        featureTypeGmlBuilder.append(">")
    }

    def _printGmlEndTag(qname) {
        if (!featureType) {
            return
        }

        featureTypeGmlBuilder.append("</${_stripGmlNamespaceFromElements(qname)}>")
    }

    def _endFeatureType(ns, localName, qname) {
        if (featureCallback) {
            try {
                if (featureType.gml) {
                    featureCallback(metadata, featureType)
                }
                else {
                    log.info("Feature type ${featureType} lacks GML")
                }
            }
            catch (Exception e) {
                metadata.error = true
            }
        }
        else {
            featureTypes << featureType
        }
        featureType = null
    }

    def _parseFeatureTypeId = { chars, offset, length ->
        featureType.featureTypeId = featureType.featureTypeId ?: idParser.parseInlineAsCharacters(featureTypeIdElementName, chars, offset, length)
    }

    def _parseGeometryElement = { chars, offset, length ->
        featureTypeGmlBuilder.append(new String(chars, offset, length))
    }

    def _endGeometryElement(ns, localName, qname) {
        featureType.gml = featureTypeGmlBuilder.toString()
    }

    def _stripGmlNamespaceFromElements(gml) {
        return gml?.replaceAll('gml:', '')
    }
}

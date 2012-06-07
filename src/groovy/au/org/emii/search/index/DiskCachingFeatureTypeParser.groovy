package au.org.emii.search.index

import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes
import org.xml.sax.InputSource;
import org.xml.sax.ext.DefaultHandler2

import au.org.emii.search.FeatureType;


class DiskCachingFeatureTypeParser extends DefaultHandler2 {

	static final Logger log = LoggerFactory.getLogger(DiskCachingFeatureTypeParser.class)
	
	def metadata
	def featureTypeElementName
	def featureTypeIdElementName
	def featureTypeGeometryElementName
	def featureCallback
	
	// State/flow controls
	def featureType
	def featureTypes
	def featureTypeGmlBuilder
	def featureCollectionStarted
	def charactersHandler
	
	DiskCachingFeatureTypeParser(GeonetworkMetadata metadata, DiskCachingFeatureTypeRequest featureTypeRequest) {
		featureTypes = []
		featureCollectionStarted = false
		this.metadata = metadata
		this.featureTypeElementName = featureTypeRequest.featureTypeElementName
		this.featureTypeIdElementName = featureTypeRequest.featureTypeIdElementName
		this.featureTypeGeometryElementName = featureTypeRequest.featureTypeGeometryElementName
		this.featureCallback = featureTypeRequest.featureCallback
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
			_endFeatureType(ns, localName, qname)
		}
		if (qname =~ /$featureTypeGeometryElementName/) {
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
	
	def _startFeatureType(ns, localName, qname, atts) {
		featureType = new FeatureType(metadata)
		if (_idIsAttribute()) {
			// This really doesn't feel robust to me but I can't find a way to
			// get the id attribute from the element using passed in parameters
			// without also looping over all the attributes
			featureType.featureTypeId = atts.getValue("gml:${_getIdAttributeName()}")
			log.debug("=> $featureType.featureTypeId")
		}
		featureTypeGmlBuilder = new StringBuilder()
	}
	
	def _printGmlStartTag(qname, atts) {
		if (!featureType) {
			return
		}
		
		featureTypeGmlBuilder.append("<").append(qname)
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
		
		featureTypeGmlBuilder.append("</").append(qname).append(">")
	}
	
	def _endFeatureType(ns, localName, qname) {
		if (featureCallback) {
			featureCallback(featureType)
		}
		else {
			featureTypes << featureType
		}
		featureType = null
	}
	
	def _parseFeatureTypeId = { chars, offset, length ->
		if (!_idIsAttribute()) {
			featureType.featureTypeId = new String(chars, offset, length)
		}
	}
	
	def _parseGeometryElement = { chars, offset, length ->
		featureTypeGmlBuilder.append(new String(chars, offset, length))
	}
	
	def _endGeometryElement(ns, localName, qname) {
		featureType.gml = featureTypeGmlBuilder.toString()
	}
	
	def _idIsAttribute() {
		return featureTypeIdElementName.startsWith("@")
	}
	
	def _getIdAttributeName() {
		return featureTypeIdElementName.substring(1)
	}
	
//	static void foo() {
//		def metadata = new GeonetworkMetadata(geonetworkUuid: "AJKHDKAJSHDAHSDJKASHDJAKHDKJASHDKA", featureTypeName: "soop_asf")
//		
//		def featureTypeRequest = new DiskCachingFeatureTypeRequest("id", "geometry")
//		featureTypeRequest.featureTypeElementName = "soop_asf"
//		
//		def handler = new DiskCachingFeatureTypeParser(metadata, featureTypeRequest)
//		def reader = SAXParserFactory.newInstance().newSAXParser().XMLReader
//		reader.setContentHandler(handler)
//		def inputReader = new BufferedReader(new FileReader("/tmp/soop_asf.xml"))
//		reader.parse(new InputSource(inputReader))
//		
//		def rand = (int)(Math.random() * (handler.featureTypes.size() -1))
//		println handler.featureTypes[rand].gml
//	}
	
}

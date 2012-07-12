package au.org.emii.search.index

class FeatureTypeIndentifierParser {
	
	def parseFromNode(featureTypeIdElementName, node) {
		return node."${featureTypeIdElementName}".text()
	}
	
	def parseInlineAsCharacters(featureTypeIdElementName, chars, offset, length) {
		def debug = new String(chars, offset, length)
		println "======== parseInlineAsCharacters => $debug"
		if (!_idIsAttribute(featureTypeIdElementName)) {
			println "$featureTypeIdElementName IS NOT an attribute"
			return new String(chars, offset, length)
		}
		else {
			println "$featureTypeIdElementName IS an attribute WTFOMGLOL"
		}
	}
	
	def parseInlineAsAttribute(featureTypeIdElementName, ns, localName, qname, atts) {
		def debug = atts.getValue(_getIdAttributeName(featureTypeIdElementName))
		println "======== parseInlineAsAttribute => $debug"
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

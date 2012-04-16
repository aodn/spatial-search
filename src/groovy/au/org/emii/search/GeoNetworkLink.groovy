package au.org.emii.search

class GeoNetworkLink {

	final int FEATURE_TYPE_INDEX = 0
	final int TITLE_INDEX = 1
	final int HREF_INDEX = 2
	final int PROTOCOL_INDEX = 3
	final int MIME_TYPE_INDEX = 4
	
	def featureType
	def title
	def href
	def protocol
	def mimeType
	
	GeoNetworkLink(link) {
		if (link) {
			def _link = link.split('\\|')
			featureType = _getToSet(_link, FEATURE_TYPE_INDEX)
			title = _getToSet(_link, TITLE_INDEX)
			href = _getToSet(_link, HREF_INDEX)
			protocol = _getToSet(_link, PROTOCOL_INDEX)
			mimeType = _getToSet(_link, MIME_TYPE_INDEX)
		}
	}
	
	def _getToSet(list, index) {
		if (list && list.size() > index) {
			return list[index]
		}
		return null
	}
}

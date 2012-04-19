package au.org.emii.search

import java.security.MessageDigest;
import java.util.Iterator;

import org.slf4j.Logger
import org.slf4j.LoggerFactory;

class GeoNetworkSearchShaBuilder {
	
	static Logger log = LoggerFactory.getLogger(GeoNetworkSearchShaBuilder.class)
	
	def grailsApplication
	def volatileParams = ['from', 'to', 'controller', 'action', 'fast']

	def buildSha(params) {
		def copy = new TreeMap(params)
		_cleanParams(copy)
		return _toSha(_buildShaInputString(copy))
	}
	
	def _cleanParams(params) {
		def knownListParams = grailsApplication.config.geonetwork.search.list.params.items
		
		for (Iterator i = params.entrySet().iterator(); i.hasNext();) {
			def entry = i.next()
			if (entry.key.startsWith('ext-comp') || volatileParams.contains(entry.key)) {
				i.remove()
			}
			if (knownListParams.contains(entry.key)) {
				params[entry.key] = _cleanListParam(entry.value)
			}
		}
	}
	
	def _cleanListParam(param) {
		def l = param.split(grailsApplication.config.geonetwork.search.list.params.delimiter)
		l = l.sort()
		return l.join(',')
	}
	
	def _buildShaInputString(params) {
		def sha = params.keySet().inject("") { result, key ->
			result += "${key}=${params[key]}&"
		}
		log.debug("SHA source string $sha")
		return sha
	}
	
	def _toSha(text) {
		def md = MessageDigest.getInstance("SHA-256")
		md.update(text.getBytes("UTF-8"))
		byte[] digest = md.digest()
		def number = new BigInteger(1, digest);
		return number.toString(16);
	}
}

package au.org.emii.search

import java.security.MessageDigest;
import java.util.Iterator;

import org.slf4j.Logger
import org.slf4j.LoggerFactory;

class GeoNetworkSearchShaBuilder {
	
	static Logger log = LoggerFactory.getLogger(GeoNetworkSearchShaBuilder.class)
	
	def volatileParams = ['from', 'to', 'controller', 'action', 'fast']

	def buildSha(params) {
		def copy = new TreeMap(params)
		_removeVolatileParams(copy)
		return _toSha(_buildShaInputString(copy))
	}
	
	def _removeVolatileParams(params) {
		volatileParams.each {
			params.remove(it)
		}
		
		for (Iterator i = params.entrySet().iterator(); i.hasNext();) {
			def entry = i.next()
			if (entry.key.startsWith('ext-comp')) {
				i.remove()
			}
		}
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

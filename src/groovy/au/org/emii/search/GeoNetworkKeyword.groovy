package au.org.emii.search

import org.apache.commons.lang.builder.CompareToBuilder;

class GeoNetworkKeyword implements Comparable<GeoNetworkKeyword> {

	def count
	def name
	def indexKey
	
	GeoNetworkKeyword() {
		count = 0
	}
	
	def increment() {
		count += 1
	}
	
	def decrement() {
		count -= 1
	}
	
	int compareTo(o) {
		return new CompareToBuilder()
			.append(count, o.count)
			.append(indexKey, o.indexKey)
			.append(name, o.name)
			.toComparison()
	}
}
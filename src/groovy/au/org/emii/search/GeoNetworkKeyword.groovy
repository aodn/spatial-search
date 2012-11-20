
/*
 * Copyright 2012 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */
package au.org.emii.search

import java.io.Serializable;
import org.apache.commons.lang.builder.CompareToBuilder;

class GeoNetworkKeyword implements Comparable<GeoNetworkKeyword>, Serializable {

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

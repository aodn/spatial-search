package au.org.emii.search.geometry

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.vividsolutions.jts.geom.GeometryFactory
import com.vividsolutions.jts.geom.PrecisionModel
import com.vividsolutions.jts.io.WKTReader

class GeometryHelper {
	
	static Logger log = LoggerFactory.getLogger(GeometryHelper.class)
	
	static final int SRID = 4326
	static final PrecisionModel PRECISION_MODEL = new PrecisionModel(PrecisionModel.FLOATING)

	def toGeometry(geometryType, text) {
		def wkt = _toWkt(geometryType, text)
		def geom = new WKTReader(new GeometryFactory(PRECISION_MODEL, SRID)).read(wkt)
		return geom
	}
	
	def _toWkt(geometryType, text) {
		try {
			return this."_to$geometryType"(text)
		}
		catch (MissingMethodException mme) {
			log.error("Could not create geometry object likely due to unsupported shape: ${geometryType}: ${mme.getMessage()}")
		}
		catch (Exception e) {
			log.error("", e)
		}
	}
	
	def _toLineString(text) {
		log.debug("Building LINESTRING")
		def coOrds = _splitCoOrdsText(text)
		def builder = new StringBuilder(5000)
		builder.append('LINESTRING (')
		for (def i = 1; i < coOrds.size(); i += 2) {
			_appendLongLatPair(builder, coOrds[i], coOrds[i - 1], ' ')
			_appendPairDelimiter(builder)
		}
		_removePairDelimiter(builder)
		builder.append(')')
		return builder.toString()
	}
	
	def _toPoint(text) {
		log.debug("Building POINT")
		def coOrds = _splitCoOrdsText(text)
		def builder = new StringBuilder(100)
		builder.append('POINT (')
		_appendLongLatPair(builder, coOrds[1], coOrds[0], ' ')
		builder.append(')')
		return builder.toString()
	}
	
	/**
	 * This doesn't actually produce a Curve geometry, it concatenates all the
	 * segments into a single LineString.  After discussion in a meeting today
	 * (2011-10-24) it was deemed that curves weren't actually represented on
	 * maps from geoserver
	 *
	 * @param geometryElement
	 * @return
	 */
	def _toCurve(text) {
		_toLineString(text)
	}
	
	def _toPolygon(text) {
		log.debug("Building POLYGON")
		def coOrds = _splitCoOrdsText(text)
		def builder = new StringBuilder(100)
		builder.append('POLYGON ((')
		for (def i = 1; i < coOrds.size(); i += 2) {
			_appendLongLatPair(builder, coOrds[i], coOrds[i - 1], ' ')
			_appendPairDelimiter(builder)
		}
		_removePairDelimiter(builder)
		builder.append('))')
		return builder.toString()
	}
	
	def _appendLongLatPair(builder, longitude, latitude, delimeter) {
		builder.append(longitude)
		builder.append(delimeter)
		builder.append(latitude)
	}
	
	def _appendPairDelimiter(builder) {
		builder.append(', ')
	}
	
	def _removePairDelimiter(builder) {
		builder.setLength(builder.length() - 2)
	}
	
	def _splitCoOrdsText(coOrdsText) {
		return coOrdsText.split(' ')
	}
}

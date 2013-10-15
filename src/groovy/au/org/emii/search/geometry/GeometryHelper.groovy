
/*
 * Copyright 2012 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */
package au.org.emii.search.geometry


import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.vividsolutions.jts.geom.GeometryFactory
import com.vividsolutions.jts.geom.PrecisionModel
import com.vividsolutions.jts.io.ParseException
import com.vividsolutions.jts.io.WKTReader

class GeometryHelper {

    static Logger log = LoggerFactory.getLogger(GeometryHelper.class)

    static final int SRID = 4326
    static final PrecisionModel PRECISION_MODEL = new PrecisionModel(PrecisionModel.FLOATING)

    static enum CoordinateFormat {
        WEST,
        SOUTH,
        EAST,
        NORTH
    }

    /*
     * Text may be a coordinate string or a list of lists of coordinate strings
     * in the case of creating a multipolygon
     */
    def toGeometryFromCoordinateText(geometryType, text) {
        def wkt = _toWkt(geometryType, text)
        log.debug(wkt)
        def geom = new WKTReader(new GeometryFactory(PRECISION_MODEL, SRID)).read(wkt)
        return geom
    }

    def toGeometryFromGmlElement(geometryType, gmlElement) {
        def text = _getCoordinateText(geometryType, gmlElement)
        return toGeometryFromCoordinateText(geometryType, text)
    }

    def toBoundingBox(orderedCoords) {
        return toBoundingBox(orderedCoords[0], orderedCoords[1], orderedCoords[2], orderedCoords[3])
    }

    def toBoundingBox(west, south, east, north) {
        def dWest = _parseToDouble(west)
        def dSouth = _parseToDouble(south)
        def dEast = _parseToDouble(east)
        def dNorth = _parseToDouble(north)


        if (!(dNorth && dEast && dSouth && dWest)) {
            throw new ParseException("Invalid bounding box parameters one of ${west} ${south} ${east} ${north} cannot be parsed to a java.lang.Double")
        }

        def geom = (dEast < dWest) ? _toMultiPolygonBoundingBox(west, south, east, north) : _toPolygonBoundingBox(west, south, east, north)
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

    def _buildGeometry(prefix, text) {
        return _buildGeometry(prefix, text, null)
    }

    def _buildGeometry(prefix, text, suffix) {
        def builder = new StringBuilder(5000)
        builder.append(prefix)
        _toCoordinateSequence(builder, text)
        if (suffix) {
            builder.append(suffix)
        }
        return builder.toString()
    }

    def _toLineString(text) {
        log.debug("Building LINESTRING")
        return _buildGeometry('LINESTRING', text)
    }

    def _toPoint(text) {
        log.debug("Building POINT")
        return _buildGeometry('POINT ', text)
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
        return _buildGeometry('POLYGON (', text, ')')
    }

    def _toMultiPolygon(sequences) {
        log.debug("Building MULTIPOLYGON")
        def builder = new StringBuilder(5000)

        builder.append('MULTIPOLYGON (')
        sequences.each { sequence ->
            builder.append('(')
            sequence.each { coords ->
                _toCoordinateSequence(builder, coords)
                _appendPairDelimiter(builder)
            }
            _removePairDelimiter(builder)
            builder.append(')')
            _appendPairDelimiter(builder)
        }
        _removePairDelimiter(builder)
        builder.append(')')
        return builder.toString()
    }

    def _toMultiPoint(text) {
        log.debug("Building MULTIPOINT")
        def pairs = []
        text.each {
            def coords = it[0].split(" ")
            pairs << coords[0] + " " + coords[1]
            pairs << coords[2] + " " + coords[3]
        }
        return 'MULTIPOINT (' + pairs.join(', ') + ')'
    }

    def _toMultiSurface(sequences) {
        return _toMultiPolygon(sequences)
    }

    def _toCoordinateSequence(builder, text) {
        String[] coords = _splitCoordsText(text)
        builder.append('(')
        for (def i = 1; i < coords.size(); i += 2) {
            _appendLongLatPair(builder, coords[i], coords[i - 1], ' ')
            _appendPairDelimiter(builder)
        }
        _removePairDelimiter(builder)
        builder.append(')')
    }

    def _appendLongLatPair(StringBuilder builder, String longitude, String latitude, String delimeter) {
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

    def _splitCoordsText(coordsText) {
        // Some tuples or coords might be comma separated as for AIMS, for a
        // quick win here just do a global find and replace, refactor as
        // necessary when other datasources cause problems
        coordsText = coordsText.replaceAll(',', ' ')
        return coordsText.split(' ')
    }

    def _parseToDouble(s) {
        try {
            return Double.valueOf(s)
        }
        catch (NumberFormatException nfe) {
            log.error("Error parsing bounding box point to Double", nfe)
        }
        return null
    }

    def _toPolygonBoundingBox(west, south, east, north) {
        return toGeometryFromCoordinateText('Polygon', "${south} ${west} ${north} ${west} ${north} ${east} ${south} ${east} ${south} ${west}")
    }

    def _toMultiPolygonBoundingBox(west, south, east, north) {
        // We are likely doing a search near the anti-meridian so we need a multipolygon
        def eastSide = ["${south} -180 ${north} -180 ${north} ${east} ${south} ${east} ${south} -180"]
        def westSide = ["${south} ${west} ${north} ${west} ${north} 180 ${south} 180 ${south} ${west}"]
        def multiPolygonSequence = [eastSide, westSide]
        return toGeometryFromCoordinateText('MultiPolygon', multiPolygonSequence)
    }

    def _getCoordinateText(geometryType, gmlElement) {
        if ('curve' == geometryType.toLowerCase()) {
            return gmlElement.Curve.segments.LineStringSegment.join(', ')
        }
        else if ('multisurface' == geometryType.toLowerCase()) {
            return _getMultiSurfaceCoordinateText(gmlElement)
        }
        return gmlElement.text()
    }

    def _getMultiSurfaceCoordinateText(gmlElement) {
        def sequences = []
        gmlElement.surfaceMember.each() { member ->
            def memberSequence = []
            member.Polygon.children().each { linearRing ->
                memberSequence << linearRing.text()
            }
            sequences << memberSequence
        }
        return sequences
    }

    def isMultiPoint(coords) {
        def multiPoint = true

        coords.each{ coord ->
            if (coord.size() > 1) {
                multiPoint = false
            }
            else {
                def text = coord[0]
                def parts = text.split(" ")
                def point1 = toGeometryFromCoordinateText('Point', "${parts[0]} ${parts[1]}")
                def point2 = toGeometryFromCoordinateText('Point', "${parts[2]} ${parts[3]}")
                if (point1 != point2) {
                    multiPoint = false
                }
            }
        }
        return multiPoint
    }

    /**
     * Reorders space delimited coord string from inputFormat to outputFormat
     *
     * @param inputFormat
     * @param outputFormat
     * @param coordString
     * @return
     */
    def orderCoordText(inputFormat, outputFormat, coordString) {
        if (inputFormat == outputFormat) {
            return coordString
        }

        def parts = coordString.split(" ")
        //should we check for size?

        return parts[inputFormat.indexOf(outputFormat[0])] + " " +
            parts[inputFormat.indexOf(outputFormat[1])] + " " +
            parts[inputFormat.indexOf(outputFormat[2])] + " " +
            parts[inputFormat.indexOf(outputFormat[3])]
    }

    def toMultiPolygonFromBoundingBoxes(boundingBoxes) {
        def sequences = []
        boundingBoxes.each { bbox ->
            def bboxList = []
            bbox.coordinates.each { coord ->
                bboxList << "${coord.x}"
                bboxList << "${coord.y}"
            }
            sequences << [bboxList.join(' ')]
        }

        return toGeometryFromCoordinateText('MultiPolygon', sequences)
    }

    def toMultiPolygonFromGeoNetworkGeoBoxes(geoBoxes) {
        def boundingBoxes = []
        geoBoxes.each { geoBox ->
            geoBox.each {
                boundingBoxes << toBoundingBox(it.split(" "))
            }
        }

        return toMultiPolygonFromBoundingBoxes(boundingBoxes)
    }
}

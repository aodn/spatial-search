
/*
 * Copyright 2012 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */
package au.org.emii.search.index.test.unit

import au.org.emii.search.index.FeatureTypeIndentifierParser

import java.sql.Timestamp

import javax.xml.parsers.SAXParserFactory

import org.xml.sax.InputSource

import au.org.emii.search.index.DiskCachingFeatureTypeParser
import au.org.emii.search.index.DiskCachingFeatureTypeRequest
import au.org.emii.search.index.GeonetworkMetadata
import grails.test.*
import groovy.xml.MarkupBuilder

class DiskCachingFeatureTypeParserTests extends GrailsUnitTestCase {

    def featureTypeRequest
    def metadata
    def handler

    def strippedGml = '<LineString srsDimension="2" srsName="http://www.opengis.net/gml/srs/epsg.xml#4326"><posList>85.4082 -66.2968 85.4082 -66.2968</posList></LineString>'

    protected void setUp() {
        super.setUp()
        mockLogging(DiskCachingFeatureTypeRequest)
        mockLogging(DiskCachingFeatureTypeParser)

        featureTypeRequest = new DiskCachingFeatureTypeRequest('id', 'geometry')
        featureTypeRequest.featureCallback = soopAsfFeatureCallback
        featureTypeRequest.metadataCallback = soopAsfMetadataCallback

        metadata = new GeonetworkMetadata(
                id: 1,
                geonetworkUuid: "fd1b7df5-7b5b-4669-9f07-302804bae527",
                featureTypeName: "imos:soop_asf",
                geoserverEndPoint: "http://geoserver.imos.org.au/geoserver",
                changeDate: new Timestamp(0)
        )
        featureTypeRequest.featureTypeElementName = metadata.featureTypeName

        handler = new DiskCachingFeatureTypeParser(metadata, featureTypeRequest)
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testSoopAsfParse() {
        def reader = SAXParserFactory.newInstance().newSAXParser().XMLReader
        reader.setContentHandler(handler)
        def inputReader = new BufferedReader(new StringReader(_getSoopAsfXml()))
        reader.parse(new InputSource(inputReader))
    }

    void testStripGmlNamespaceFromStartElements() {
        assertEquals(
            strippedGml,
            handler._stripGmlNamespaceFromElements('<gml:LineString srsDimension="2" srsName="http://www.opengis.net/gml/srs/epsg.xml#4326"><gml:posList>85.4082 -66.2968 85.4082 -66.2968</posList></LineString>')
        )
    }

    void testStripGmlNamespaceFromEndElements() {
        assertEquals(
            strippedGml,
            handler._stripGmlNamespaceFromElements('<LineString srsDimension="2" srsName="http://www.opengis.net/gml/srs/epsg.xml#4326"><posList>85.4082 -66.2968 85.4082 -66.2968</gml:posList></gml:LineString>')
        )
    }

    void testStripGmlNamespaceFromAllElements() {
        assertEquals(
            strippedGml,
            handler._stripGmlNamespaceFromElements('<gml:LineString srsDimension="2" srsName="http://www.opengis.net/gml/srs/epsg.xml#4326"><gml:posList>85.4082 -66.2968 85.4082 -66.2968</gml:posList></gml:LineString>')
        )
    }

    void testPrintGmlStartTag() {
        startFeatureTypeParse()
        handler._printGmlStartTag('LineString', mockGmlElementAttributes())
        assertEquals('<LineString srsDimension="2">', handler.featureTypeGmlBuilder.toString())
    }

    void testPrintGmlStartTagWithStripping() {
        startFeatureTypeParse()
        handler._printGmlStartTag('gml:LineString', mockGmlElementAttributes())
        assertEquals('<LineString srsDimension="2">', handler.featureTypeGmlBuilder.toString())
    }

    void testPrintGmlEndTag() {
        startFeatureTypeParse()
        handler._printGmlEndTag('LineString')
        assertEquals('</LineString>', handler.featureTypeGmlBuilder.toString())
    }

    void testPrintGmlEndTagWithStripping() {
        startFeatureTypeParse()
        handler._printGmlEndTag('gml:LineString')
        assertEquals('</LineString>', handler.featureTypeGmlBuilder.toString())
    }

    def soopAsfFeatureCallback = { metadata, featureType ->
        assertTrue featureType.featureTypeId && featureType.featureTypeId.trim().length() > 0
        assertTrue featureType.gml && featureType.gml.trim().length() > 0
    }

    def soopAsfMetadataCallback = { metadata ->
        assertTrue null == metadata.error
    }

    def startFeatureTypeParse() {
        FeatureTypeIndentifierParser.metaClass.parseInlineAsAttribute = { "1" }
        handler._startFeatureType('gml', 'id', '', '')
        /*
            _startFeatureType(ns, localName, qname, atts) {
        featureType = new FeatureType(metadata)
        featureType.featureTypeId = featureType.featureTypeId ?: idParser.parseInlineAsAttribute(featureTypeIdElementName, ns, localName, qname, atts)
        featureTypeGmlBuilder = new StringBuilder()
         */
    }

    def mockGmlElementAttributes() {
        return [
                getLength: { -> 1 },
                getLocalName: { i -> "srsDimension" },
                getValue: { i -> "2" }
        ]
    }

    def _getSoopAsfXml() {
        def out = new StringWriter()
        def builder = new MarkupBuilder(new PrintWriter(out))."wfs:FeatureCollection"(
            xmlns: "http://www.opengis.net/wfs",
            "xmlns:wfs": "http://www.opengis.net/wfs",
            "xmlns:imos": "imos",
            "xmlns:gml": "http://www.opengis.net/gml",
            "xmlns:xsi": "http://www.w3.org/2001/XMLSchema-instance",
            "xsi:schemaLocation": "imos http://geoserver.imos.org.au:80/geoserver/wfs?service=WFS&amp;version=1.0.0&amp;request=DescribeFeatureType&amp;typeName=imos%3Asoop_asf http://www.opengis.net/wfs http://geoserver.imos.org.au:80/geoserver/schemas/wfs/1.0.0/WFS-basic.xsd"
        )
        {
            "gml:boundedBy" {
                "gml:null"("unknown")
            }
            "gml:featureMember" {
                "imos:soop_asf"(fid: "soop_asf.fid-22ac096e_1388d6e6bd9_6072") {
                    "imos:id"("367408")
                    "imos:geometry" {
                        "gml:LineString"(srsName: "http://www.opengis.net/gml/srs/epsg.xml#4326") {
                            "gml:coordinates"("xmlns:gml": "http://www.opengis.net/gml", decimal: ".", cs: ",", ts: " ",
                                "152.46499634,-33.17089844 152.46600342,-33.16579819 152.46600342,-33.16189957 152.46699524,-33.15769958 152.46800232,-33.15430069 152.46800232,-33.15029907 152.46899414,-33.14590073 152.47000122,-33.14160156 152.47900391,-33.13040161 152.49099731,-33.12250137 152.50300598,-33.11360168 152.51499939,-33.10469818 152.5269928,-33.09600067 152.53900146,-33.08720016 152.55200195,-33.0788002 152.56399536,-33.0705986 152.57600403,-33.06230164 152.58799744,-33.05410004 152.60099792,-33.04589844 152.61300659,-33.03770065 152.625,-33.02949905 152.63699341,-33.02140045 152.6499939,-33.01340103 152.66099548,-33.00500107 152.67300415,-32.99739838 152.68499756,-32.9905014 152.69599915,-32.98270035 152.70899963,-32.97449875 152.71299744,-32.9715004 152.71200562,-32.96319962 152.70300293,-32.95650101 152.69299316,-32.95000076 152.68400574,-32.94319916 152.67500305,-32.93700027 152.66600037,-32.93149948 152.65800476,-32.92620087 152.64900208,-32.92050171 152.64100647,-32.91460037 152.63299561,-32.90819931 152.62399292,-32.90159988 152.61700439,-32.89609909 152.61099243,-32.89199829 152.60499573,-32.88759995 152.60800171,-32.89369965 152.61199951,-32.89619827 152.60400391,-32.8905983 152.59599304,-32.88460159 152.58700562,-32.8783989 152.57899475,-32.87239838 152.57000732,-32.86579895 152.56100464,-32.85950089 152.55299377,-32.85350037 152.54400635,-32.84700012 152.53399658,-32.84069824 152.5249939,-32.83430099 152.51499939,-32.82799911 152.50500488,-32.8144989 152.50700378,-32.81259918 152.51899719,-32.82920074 152.53199768,-32.83959961 152.54600525,-32.84870148 152.55999756,-32.85739899 152.57400513,-32.86660004 152.58500671,-32.87530136 152.6000061,-32.88589859 152.5980072,-32.88679886 152.59700012,-32.8893013 152.59500122,-32.89139938 152.59300232,-32.89440155 152.59100342,-32.89789963 152.58900452,-32.90100098 152.58200073,-32.89789963"
                            )
                        }
                    }
                }
            }
            "gml:featureMember" {
                "imos:soop_asf"(fid: "soop_asf.fid-22ac096e_1388d6e6bd9_6073") {
                    "imos:id"("367409")
                    "imos:geometry" {
                        "gml:LineString"(srsName: "http://www.opengis.net/gml/srs/epsg.xml#4326") {
                            "gml:coordinates"("xmlns:gml": "http://www.opengis.net/gml", decimal: ".", cs: ",", ts: " ",
                                "153.31599426,-31.01440048 153.32099915,-31.00390053 153.32499695,-30.99329948 153.32899475,-30.98270035 153.33299255,-30.97200012 153.33799744,-30.96159935 153.34199524,-30.9510994 153.34599304,-30.94059944 153.3500061,-30.93009949 153.35499573,-30.91970062 153.35899353,-30.90920067 153.36300659,-30.8987999 153.36700439,-30.88820076 153.37199402,-30.87759972 153.37600708,-30.86709976 153.3809967,-30.85689926 153.38600159,-30.8465004 153.39100647,-30.83620071 153.39599609,-30.81450081 153.39700317,-30.80340004 153.39900208,-30.79240036 153.3999939,-30.78149986 153.40100098,-30.77039909 153.4019928,-30.7595005 153.40299988,-30.74850082 153.40499878,-30.73749924 153.40600586,-30.72649956 153.40699768,-30.71549988 153.40800476,-30.7045002 153.40899658,-30.6935997 153.41099548,-30.68280029 153.41200256,-30.6718998 153.41299438,-30.6609993 153.41400146,-30.64999962 153.41499329,-30.63899994 153.41600037,-30.62809944 153.41799927,-30.61750031 153.42199707,-30.60709953 153.42599487,-30.59659958 153.42999268,-30.58609962 153.43400574,-30.57550049 153.43800354,-30.56500053 153.44200134,-30.55450058 153.44599915,-30.54389954 153.44999695,-30.5333004 153.45399475,-30.52269936 153.45799255,-30.51210022 153.46099854,-30.50139999 153.46299744,-30.49040031 153.46499634,-30.47920036 153.46600342,-30.46820068 153.46800232,-30.45709991 153.47000122,-30.44549942 153.47200012,-30.43370056 153.47399902,-30.4218998 153.4750061,-30.41020012 153.477005,-30.39859962 153.47900391,-30.38689995 153.48100281,-30.37540054 153.48300171,-30.36389923 153.48500061,-30.35239983 153.48599243,-30.34090042 153.48899841,-30.33040047 153.49299622,-30.32010078"
                            )
                        }
                    }
                }
            }
            "gml:featureMember" {
                "imos:soop_asf"(fid: "soop_asf.fid-22ac096e_1388d6e6bd9_615f") {
                    "imos:id"("390726")
                }
            }
        }
        println out
        return out.toString()
    }
}

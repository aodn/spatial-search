package au.org.emii.search.index

import com.vividsolutions.jts.io.WKTReader;

import grails.test.*

class FeatureTypeRequestServiceTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }
	
	void testNoGeometry() {
		def xml = """<wfs:FeatureCollection numberOfFeatures="1" timeStamp="2011-12-13T09:30:58.239+11:00" xsi:schemaLocation="http://www.openplans.org/topp http://www.opengis.net/wfs http://geoserver.emii.org.au:80/geoserver/schemas/wfs/1.1.0/wfs.xsd" xmlns:ogc="http://www.opengis.net/ogc" xmlns:gml="http://www.opengis.net/gml" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:topp="http://www.openplans.org/topp" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:ows="http://www.opengis.net/ows" xmlns:wfs="http://www.opengis.net/wfs">
  <gml:featureMembers>
    <topp:soop_sst_mv gml:id="soop_sst_mv.fid--5a9a92e8_134346692e9_-7f29">
      <topp:id>366135</topp:id>
      <topp:callsign>VHW5167</topp:callsign>
      <topp:vessel_name>M/V SeaFlyte</topp:vessel_name>
      <topp:principal_investigator>Helen Beggs</topp:principal_investigator>
      <topp:opendap_url>http://opendap-tpac.arcs.org.au/thredds/dodsC/IMOS/SOOP/SOOP-SST/VHW5167_Sea-Flyte/2011/IMOS_SOOP-SST_T_20110904T235900Z_VHW5167_FV01.nc</topp:opendap_url>
      <topp:download_url>http://opendap-tpac.arcs.org.au/thredds/fileServer/IMOS/SOOP/SOOP-SST/VHW5167_Sea-Flyte/2011/IMOS_SOOP-SST_T_20110904T235900Z_VHW5167_FV01.nc</topp:download_url>
      <topp:dataset_uuid>b0d62dab-0352-4186-88ee-77514de196d4</topp:dataset_uuid>
      <topp:platform_uuid>e5bce384-c5f3-4c97-9fef-8859e500534e</topp:platform_uuid>
      <topp:delayed_mode>false</topp:delayed_mode>
      <topp:geospatial_vertical_min>0.0</topp:geospatial_vertical_min>
      <topp:geospatial_vertical_max>0.0</topp:geospatial_vertical_max>
      <topp:time_coverage_start>2011-09-04T23:59:00+10:00</topp:time_coverage_start>
      <topp:time_coverage_end>2011-09-04T23:59:00+10:00</topp:time_coverage_end>
    </topp:soop_sst_mv>
  </gml:featureMembers>
</wfs:FeatureCollection>"""
		
		def featureTypeRequest = new FeatureTypeRequest() 
		def tree = new XmlSlurper().parseText(xml)
		tree.featureMembers[0]."soop_sst_mv".each { featureTree ->
			def geometry = featureTypeRequest._toGeometry(featureTree."geometry")
			assertTrue geometry == null
		}
	}
	
	void testHasGeometry() {
		def xml = """<wfs:FeatureCollection numberOfFeatures="1" timeStamp="2011-12-13T09:30:58.239+11:00" xsi:schemaLocation="http://www.openplans.org/topp http://www.opengis.net/wfs http://geoserver.emii.org.au:80/geoserver/schemas/wfs/1.1.0/wfs.xsd" xmlns:ogc="http://www.opengis.net/ogc" xmlns:gml="http://www.opengis.net/gml" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:topp="http://www.openplans.org/topp" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:ows="http://www.opengis.net/ows" xmlns:wfs="http://www.opengis.net/wfs">
  <gml:featureMembers>
	<topp:soop_sst_mv gml:id="soop_sst_mv.fid--5a9a92e8_1343492c9ff_2a71">
      <topp:id>112412</topp:id>
      <topp:callsign>9HA2479</topp:callsign>
      <topp:vessel_name>Pacific Sun</topp:vessel_name>
      <topp:principal_investigator>Helen Beggs</topp:principal_investigator>
      <topp:opendap_url>http://opendap-tpac.arcs.org.au/thredds/dodsC/IMOS/SOOP/SOOP-SST/9HA2479_Pacific-Sun/2010/IMOS_SOOP-SST_MT_20101212T120000Z_9HA2479_FV01.nc</topp:opendap_url>
      <topp:download_url>http://opendap-tpac.arcs.org.au/thredds/fileServer/IMOS/SOOP/SOOP-SST/9HA2479_Pacific-Sun/2010/IMOS_SOOP-SST_MT_20101212T120000Z_9HA2479_FV01.nc</topp:download_url>
      <topp:dataset_uuid>23d61cee-417e-4bce-ad62-4b2810d5ef8d</topp:dataset_uuid>
      <topp:platform_uuid>05d4f14b-9e35-4f74-9635-276e455f566f</topp:platform_uuid>
      <topp:delayed_mode>false</topp:delayed_mode>
      <topp:geospatial_lat_min>-33.9</topp:geospatial_lat_min>
      <topp:geospatial_lon_min>151.2</topp:geospatial_lon_min>
      <topp:geospatial_lat_max>-30.7</topp:geospatial_lat_max>
      <topp:geospatial_lon_max>155.6</topp:geospatial_lon_max>
      <topp:geospatial_vertical_min>0.0</topp:geospatial_vertical_min>
      <topp:geospatial_vertical_max>0.0</topp:geospatial_vertical_max>
      <topp:geometry>
        <gml:LineString srsDimension="2" srsName="urn:x-ogc:def:crs:EPSG:4326">
          <gml:posList>
-33.900001525878906 151.1999969482422 -32.599998474121094 153.0 -32.5 153.1999969482422 -32.29999923706055 153.39999389648438 -32.099998474121094 153.6999969482422 -31.899999618530273 153.89999389648438 -31.799999237060547 154.1999969482422 -31.5 154.39999389648438 -31.299999237060547 154.6999969482422 -31.100000381469727 155.0 -30.700000762939453 155.60000610351562
          </gml:posList>
        </gml:LineString>
      </topp:geometry>
      <topp:time_coverage_start>2010-12-12T12:00:00+11:00</topp:time_coverage_start>
      <topp:time_coverage_end>2010-12-12T23:00:00+11:00</topp:time_coverage_end>
    </topp:soop_sst_mv>
  </gml:featureMembers>
</wfs:FeatureCollection>"""
		
		def featureTypeRequest = new FeatureTypeRequest()
		def tree = new XmlSlurper().parseText(xml)
		tree.featureMembers[0]."soop_sst_mv".each { featureTree ->
			def geometry = featureTypeRequest._toGeometry(featureTree."geometry")
			assertTrue geometry != null
		}
	}
}

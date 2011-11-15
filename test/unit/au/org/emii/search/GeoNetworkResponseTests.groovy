package au.org.emii.search

import grails.test.*

import org.codehaus.groovy.grails.commons.GrailsApplication

import au.org.emii.search.index.GeonetworkMetadata;

class GeoNetworkResponseTests extends GrailsUnitTestCase {
    
	def responseXml = """<?xml version="1.0" encoding="UTF-8"?>
<response from="1" to="9" selected="0">
    <summary count="37" type="local" hitsusedforsummary="37">
        <dataParameters>
            <dataParameter count="1" name="Turbidity" />
            <dataParameter count="1" name="wind_from_direction" />
            <dataParameter count="1" name="Sea surface temperature" />
            <dataParameter count="1" name="volumetric_backscatter_coefficient" />
            <dataParameter count="1" name="air_pressure" />
            <dataParameter count="1" name="Taxonomic Group Name" />
            <dataParameter count="1" name="Sea water temperature" />
            <dataParameter count="1" name="sea_water_temperature" />
            <dataParameter count="1" name="sea_water_electrical_conductivity" />
            <dataParameter count="1" name="fluorescence" />
        </dataParameters>
        <keywords>
            <keyword count="14" name="SHIPS |" />
            <keyword count="14" name="ECHO SOUNDERS |" />
            <keyword count="14" name="ATMOSPHERE | ATMOSPHERIC TEMPERATURE | AIR TEMPERATURE |  |" />
            <keyword count="14" name="Oceans | Ocean Temperature | Water Temperature" />
            <keyword count="14" name="AMD" />
            <keyword count="14" name="ATMOSPHERE | ATMOSPHERIC PRESSURE | SEA LEVEL PRESSURE |  |" />
            <keyword count="14" name="ATMOSPHERE | ATMOSPHERIC RADIATION | SOLAR RADIATION |  |" />
            <keyword count="14" name="ATMOSPHERE | ATMOSPHERIC WATER VAPOR | HUMIDITY |  |" />
            <keyword count="14" name="METEOROLOGY" />
            <keyword count="14" name="MARINE" />
        </keywords>
        <organizationNames>
            <organizationName count="14" name="AU/AADC | Australian Antarctic Data Centre, Australia" />
            <organizationName count="8" name="CSIRO Marine and Atmospheric Research" />
            <organizationName count="4" name="eMarine Information Infrastructure (eMII)" />
            <organizationName count="4" name="eMarine Information Infrastructure" />
            <organizationName count="3" name="School of Environmental Systems Engineering, University of Western Australia" />
            <organizationName count="3" name="Australian Bureau of Meteorology" />
            <organizationName count="1" name="University of Tasmania" />
            <organizationName count="1" name="James Cook University" />
            <organizationName count="1" name="Australian Centre for Field Robotics" />
            <organizationName count="1" name="Environment Protection Authority Victoria (EPA Vic)" />
        </organizationNames>
    </summary>
    <metadata>
        <title>Physiographic Map of North and Central Eurasia (Sample record, please remove!)</title>
        <abstract>Physiographic maps for the CIS and Baltic States (CIS_BS), Mongolia, China and Taiwan Province of China. Between the three regions (China, Mongolia, and CIS_BS countries) DCW boundaries were introduced. There are no DCW boundaries between Russian Federation and the rest of the new countries of the CIS_BS. The original physiographic map of China includes the Chinese border between India and China, which extends beyond the Indian border line, and the South China Sea islands (no physiographic information is present for islands in the South China Sea). The use of these country boundaries does not imply the expression of any opinion whatsoever on the part of FAO concerning the legal or constitutional states of any country, territory, or sea area, or concerning delimitation of frontiers. The Maps visualize the items LANDF, HYPSO, SLOPE that correspond to Landform, Hypsometry and Slope.</abstract>
        <keyword>physiography, soil</keyword>
        <keyword>Eurasia</keyword>
        <geoBox>
            <westBL>37</westBL>
            <eastBL>156</eastBL>
            <southBL>-3</southBL>
            <northBL>83</northBL>
        </geoBox>
        <link title="Physiography of North and Central Eurasia Landform (Gif Format)" href="http://localhost:8080/geonetwork/srv/en/resources.get?id=10&amp;fname=phy.zip&amp;access=private" name="phy.zip" protocol="WWW:DOWNLOAD-1.0-http--download" type="application/zip" />
        <link type="download">http://localhost:8080/geonetwork/srv/en/resources.get?id=10&amp;fname=phy.zip&amp;access=private</link>
        <link title="Physiography of North and Central Eurasia Landform" href="http://geonetwork3.fao.org/ows/7386_landf" name="landform" protocol="OGC:WMS-1.1.1-http-get-map" type="application/vnd.ogc.wms_xml" />
        <link title="Physiography of North and Central Eurasia Landform" href="http://localhost:8080/geonetwork/srv/en/google.kml?uuid=d26c4828-5cfc-4872-b998-ffd44f18cd31&amp;layers=landform" name="landform" type="application/vnd.google-earth.kml+xml" />
        <link type="wms">javascript:addWMSLayer([["landform","http://geonetwork3.fao.org/ows/7386_landf", "landform","317184"]])</link>
        <link type="googleearth">/geonetwork/srv/en/google.kml?uuid=d26c4828-5cfc-4872-b998-ffd44f18cd31&amp;layers=landform</link>
        <link title="Physiography of North and Central Eurasia Slope" href="http://geonetwork3.fao.org/ows/7386_slope" name="slope" protocol="OGC:WMS-1.1.1-http-get-map" type="application/vnd.ogc.wms_xml" />
        <link title="Physiography of North and Central Eurasia Slope" href="http://localhost:8080/geonetwork/srv/en/google.kml?uuid=d26c4828-5cfc-4872-b998-ffd44f18cd31&amp;layers=slope" name="slope" type="application/vnd.google-earth.kml+xml" />
        <link type="wms">javascript:addWMSLayer([["slope","http://geonetwork3.fao.org/ows/7386_slope", "slope","317184"]])</link>
        <link type="googleearth">/geonetwork/srv/en/google.kml?uuid=d26c4828-5cfc-4872-b998-ffd44f18cd31&amp;layers=slope</link>
        <link title="Physiography of North and Central Eurasia Hypsography" href="http://geonetwork3.fao.org/ows/7386_hypso" name="hypsography" protocol="OGC:WMS-1.1.1-http-get-map" type="application/vnd.ogc.wms_xml" />
        <link title="Physiography of North and Central Eurasia Hypsography" href="http://localhost:8080/geonetwork/srv/en/google.kml?uuid=d26c4828-5cfc-4872-b998-ffd44f18cd31&amp;layers=hypsography" name="hypsography" type="application/vnd.google-earth.kml+xml" />
        <link type="wms">javascript:addWMSLayer([["hypsography","http://geonetwork3.fao.org/ows/7386_hypso", "hypsography","317184"]])</link>
        <link type="googleearth">/geonetwork/srv/en/google.kml?uuid=d26c4828-5cfc-4872-b998-ffd44f18cd31&amp;layers=hypsography</link>
        <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
            <id xmlns:gmd="http://www.isotc211.org/2005/gmd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:gml="http://www.opengis.net/gml" xmlns:gts="http://www.isotc211.org/2005/gts" xmlns:gco="http://www.isotc211.org/2005/gco">317184</id>
            <uuid xmlns:gmd="http://www.isotc211.org/2005/gmd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:gml="http://www.opengis.net/gml" xmlns:gts="http://www.isotc211.org/2005/gts" xmlns:gco="http://www.isotc211.org/2005/gco">d26c4828-5cfc-4872-b998-ffd44f18cd31</uuid>
        </geonet:info>
    </metadata>
    <metadata>
        <title>IMOS SOOP-Air Sea Flux (ASF) sub facility</title>
        <abstract>Enhancement of Measurements on Ships of Opportunity (SOOP)-Air Sea Flux sub-facility collects underway meteorological and oceanographic observations during scientific  and Antarctic resupply voyages in the oceans adjacent to Australia. Data products are quality controlled observations, and bulk air-sea fluxes.

Research Vessel Real Time Air-Sea Fluxes, aims to equip the Marine National Facility (MNF) (Research Vessel Southern Surveyor) and the Research and Supply Vessel, Aurora Australis with "climate quality" meteorological measurement systems, capable of providing high quality air-sea flux measurements and delivered to researchers on a near real-time basis. Obtaining the full set of air-sea fluxes essential for climate studies requires observations of: wind, air and sea temperature, humidity, pressure, precipitation, long- and short-wave radiation. The existing ship meteorological sensor sets have been completed with IMOS instruments and a comprehensive annual calibration programs implemented. The ships have dual sets of radiometers, temperature and humidity sensors, precipitation gauges (optical rain gauge and Siphon gauge) and anemometers (2-d sonic and prop/vane - Southern Surveyor only)  . The Aurora Australis will be fitted with two long-wave radiometers and one ORG (serviced during winter port time). Data streams are fed into the existing ship data management system, and broadcast via satellite back to Australia routinely. The observations are quality controlled at the Bureau of Meteorology and air-sea fluxes calculated using the COARE Bulk Flux algorithm. 

A daily file of 1-minute averages of the observations and a daily file of 5-minute average calculated bulk fluxes are generated shortly after 0000UTC and provided to eMII.</abstract>
        <keyword>Oceans | Ocean Temperature | Sea Surface Temperature</keyword>
        <keyword>Atmosphere | Atmospheric Temperature | Surface Air Temperature</keyword>
        <keyword>Atmosphere | Atmospheric Pressure | Surface Pressure</keyword>
        <keyword>Atmosphere | Radiation Budget | Shortwave Radiation</keyword>
        <keyword>Atmosphere | Radiation Budget | Longwave Radiation</keyword>
        <keyword>Atmosphere | Precipitation | Precipitation Amount</keyword>
        <keyword>Atmosphere | Precipitation | Precipitation Rate</keyword>
        <keyword>Atmosphere | Atmospheric Water Vapor | Humidity</keyword>
        <keyword>Atmosphere | Atmospheric Winds | Surface Winds</keyword>
        <keyword>Oceans | Ocean Optics | Photosynthetically Active Radiation</keyword>
        <keyword>Atmosphere | Radiation Budget | Heat Flux</keyword>
        <keyword>Global/Oceans | Pacific Ocean</keyword>
        <keyword>Global/Oceans | Southern Ocean</keyword>
        <keyword>Regional Seas | Tasman Sea</keyword>
        <keyword>Regional Seas | Coral Sea</keyword>
        <geoBox>
            <westBL>80</westBL>
            <eastBL>180</eastBL>
            <southBL>-70</southBL>
            <northBL>0</northBL>
        </geoBox>
        <link title="Point of truth URL of this metadata record" href="http://localhost:8080/geonetwork/srv/en/metadata.show?uuid=fd1b7df5-7b5b-4669-9f07-302804bae527" name="" protocol="WWW:LINK-1.0-http--metadata-URL" type="text/html" />
        <link type="url">http://localhost:8080/geonetwork/srv/en/metadata.show?uuid=fd1b7df5-7b5b-4669-9f07-302804bae527</link>
        <link title="Search for any child record [opens IMOS Metadata Catalogue]" href="http://imosmest.emii.org.au/geonetwork/srv/en/search.external?any=fd1b7df5-7b5b-4669-9f07-302804bae527" name="" protocol="WWW:LINK-1.0-http--link" type="text/html" />
        <link type="url">http://imosmest.emii.org.au/geonetwork/srv/en/search.external?any=fd1b7df5-7b5b-4669-9f07-302804bae527</link>
        <link title="SOOP Air Sea Fluxes" href="http://geoserverdev.emii.org.au/geoserver/wms" name="topp:soop_asf" protocol="OGC:WMS-1.1.1-http-get-map" type="application/vnd.ogc.wms_xml" />
        <link title="SOOP Air Sea Fluxes" href="http://localhost:8080/geonetwork/srv/en/google.kml?uuid=fd1b7df5-7b5b-4669-9f07-302804bae527&amp;layers=topp:soop_asf" name="topp:soop_asf" type="application/vnd.google-earth.kml+xml" />
        <link type="wms">javascript:addWMSLayer([["topp:soop_asf","http://geoserverdev.emii.org.au/geoserver/wms", "topp:soop_asf","4537"]])</link>
        <link type="googleearth">/geonetwork/srv/en/google.kml?uuid=fd1b7df5-7b5b-4669-9f07-302804bae527&amp;layers=topp:soop_asf</link>
        <link title="SOOP Air Sea Fluxes (7 days)" href="http://geoserverdev.emii.org.au/geoserver/wms" name="topp:soop_asf_recent" protocol="OGC:WMS-1.1.1-http-get-map" type="application/vnd.ogc.wms_xml" />
        <link title="SOOP Air Sea Fluxes (7 days)" href="http://localhost:8080/geonetwork/srv/en/google.kml?uuid=fd1b7df5-7b5b-4669-9f07-302804bae527&amp;layers=topp:soop_asf_recent" name="topp:soop_asf_recent" type="application/vnd.google-earth.kml+xml" />
        <link type="wms">javascript:addWMSLayer([["topp:soop_asf_recent","http://geoserverdev.emii.org.au/geoserver/wms", "topp:soop_asf_recent","4537"]])</link>
        <link type="googleearth">/geonetwork/srv/en/google.kml?uuid=fd1b7df5-7b5b-4669-9f07-302804bae527&amp;layers=topp:soop_asf_recent</link>
        <geonet:info xmlns:geonet="http://www.fao.org/geonetwork" xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
            <id xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:gml="http://www.opengis.net/gml" xmlns:gts="http://www.isotc211.org/2005/gts" xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:gmd="http://www.isotc211.org/2005/gmd">4537</id>
            <uuid xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:gml="http://www.opengis.net/gml" xmlns:gts="http://www.isotc211.org/2005/gts" xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:gmd="http://www.isotc211.org/2005/gmd">fd1b7df5-7b5b-4669-9f07-302804bae527</uuid>
        </geonet:info>
    </metadata>
    <metadata>
        <title>IMOS SOOP-Sea Surface Temperature (SST) Sub-facility</title>
        <abstract>The Sea Surface Temperature (SST) sub-facility aims to enable accurate, quality controlled, SST data to be supplied in near real-time (within 24 hours) from SOOPs and research vessels in the Australian region.

Remotely sensed sea surface temperature (SST) data is an important input to ocean, numerical weather prediction, seasonal and climate models.  In order to improve calibration and validation of satellite SST in the Australian region, there is a need for high quality in situ SST observations with greater timeliness, spatial and temporal coverage than is currently available.  Regions particularly lacking in moored or drifting buoy observations are the Western Pacific Tropical Warm Pool region (Indonesia), close to the Australian coast (including Bass Strait) and the Southern Ocean.  Current ship SST observations from ships of opportunity (SOOP) are either of questionable accuracy or difficult to access in a timely manner.  

There are eight vessels carrying automatic weather stations (AWS) that participate in the Australian Volunteer Observing Fleet (AVOF) program.  Their routes include the Southern Ocean, coastal Australia (Queensland to South Australia), Bass Strait, North Pacific Ocean and the Tasman Sea.  On the AVOF vessels hull-mounted temperature sensors (Sea Bird SBE 48) will supply high-quality bulk SST data every one  or three hours.  There are also three passenger ferries that are currently taking SST measurements using thermosalinographs, a radiometer and a PRT (MV SeaFlyte - Fremantle-Rottnest Island, MV Fantasea Wonder - Whitsunday Island to Hardy Reef, MV Reef Voyager - Gladstone to Heron Island).  In addition, there are now near real-time SST data streams available from two Australian research vessels (RV Southern Surveyor and SRV Aurora Australis).  In total, thirteen vessels.  All SST data shall be quality assured, placed on the Global Telecommunications System (GTS) and fed into the Bureau of Meteorology's near real-time satellite SST data validation system and operational regional and global SST analyses.</abstract>
        <keyword>Oceans | Ocean Temperature | Sea Surface Temperature</keyword>
        <keyword>Oceans | Ocean Winds | Surface Winds</keyword>
        <keyword>Atmosphere | Atmospheric Temperature | Air Temperature</keyword>
        <keyword>Atmosphere | Atmospheric Pressure | Atmospheric Pressure</keyword>
        <keyword>Atmosphere | Atmospheric Water Vapor | Dew Point</keyword>
        <keyword>Global/Oceans | Pacific Ocean</keyword>
        <keyword>Global/Oceans | Southern Ocean</keyword>
        <keyword>Global/Oceans | Indian Ocean</keyword>
        <keyword>Regional Seas | Tasman Sea</keyword>
        <keyword>Regional Seas | Coral Sea</keyword>
        <keyword>Marine Features (Australia) | Bass Strait</keyword>
        <keyword>Marine Features (Australia) | Great Barrier Reef</keyword>
        <geoBox>
            <westBL>60</westBL>
            <eastBL>190</eastBL>
            <southBL>-65</southBL>
            <northBL>35</northBL>
        </geoBox>
        <link title="Point of truth URL of this metadata record" href="http://mesttest.emii.org.au:80/geonetwork/srv/en/metadata.show?uuid=3999f117-b50e-4b5e-92e9-82ed04a736d5" name="" protocol="WWW:LINK-1.0-http--metadata-URL" type="text/html" />
        <link type="url">http://mesttest.emii.org.au:80/geonetwork/srv/en/metadata.show?uuid=3999f117-b50e-4b5e-92e9-82ed04a736d5</link>
        <link title="Search for any child record [opens IMOS Metadata catalogue]" href="http://imosmest.emii.org.au/geonetwork/srv/en/search.external?any=3999f117-b50e-4b5e-92e9-82ed04a736d5" name="" protocol="WWW:LINK-1.0-http--link" type="text/html" />
        <link type="url">http://imosmest.emii.org.au/geonetwork/srv/en/search.external?any=3999f117-b50e-4b5e-92e9-82ed04a736d5</link>
        <link title="SOOP SST (ALL Tracks since the beginning of IMOS)" href="http://geoserverdev.emii.org.au/geoserver/wms" name="topp:soop_sst_without_1min_vw" protocol="OGC:WMS-1.1.1-http-get-map" type="application/vnd.ogc.wms_xml" />
        <link title="SOOP SST (ALL Tracks since the beginning of IMOS)" href="http://localhost:8080/geonetwork/srv/en/google.kml?uuid=3999f117-b50e-4b5e-92e9-82ed04a736d5&amp;layers=topp:soop_sst_without_1min_vw" name="topp:soop_sst_without_1min_vw" type="application/vnd.google-earth.kml+xml" />
        <link type="wms">javascript:addWMSLayer([["topp:soop_sst_without_1min_vw","http://geoserverdev.emii.org.au/geoserver/wms", "topp:soop_sst_without_1min_vw","4485"]])</link>
        <link type="googleearth">/geonetwork/srv/en/google.kml?uuid=3999f117-b50e-4b5e-92e9-82ed04a736d5&amp;layers=topp:soop_sst_without_1min_vw</link>
        <link title="SOOP SST (All recent tracks)" href="http://geoserverdev.emii.org.au/geoserver/wms" name="topp:soop_sst" protocol="OGC:WMS-1.1.1-http-get-map" type="application/vnd.ogc.wms_xml" />
        <link title="SOOP SST (All recent tracks)" href="http://localhost:8080/geonetwork/srv/en/google.kml?uuid=3999f117-b50e-4b5e-92e9-82ed04a736d5&amp;layers=topp:soop_sst" name="topp:soop_sst" type="application/vnd.google-earth.kml+xml" />
        <link type="wms">javascript:addWMSLayer([["topp:soop_sst","http://geoserverdev.emii.org.au/geoserver/wms", "topp:soop_sst","4485"]])</link>
        <link type="googleearth">/geonetwork/srv/en/google.kml?uuid=3999f117-b50e-4b5e-92e9-82ed04a736d5&amp;layers=topp:soop_sst</link>
        <geonet:info xmlns:geonet="http://www.fao.org/geonetwork" xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
            <id xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:gml="http://www.opengis.net/gml" xmlns:gts="http://www.isotc211.org/2005/gts" xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:gmd="http://www.isotc211.org/2005/gmd">4485</id>
            <uuid xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:gml="http://www.opengis.net/gml" xmlns:gts="http://www.isotc211.org/2005/gts" xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:gmd="http://www.isotc211.org/2005/gmd">3999f117-b50e-4b5e-92e9-82ed04a736d5</uuid>
        </geonet:info>
    </metadata>
    <metadata>
        <title>IMOS - Australian National Mooring Network (ANMN) Facility</title>
        <abstract>The Australian National Mooring Network Facility is a series of national reference stations and regional moorings designed to monitor particular oceanographic phenomena in Australian coastal ocean waters. 

There are seven sub-facilities in the ANMN: four regional sub-facilities, a series of National Reference Stations (NRS), Acoustic Observatories and a Satellite Ocean Colour Calibration sub-facility.

The ANMN sub-facilities are:
a) Queensland and Northern Australia
b) New South Wales
c) Southern Australia
d) Western Australia
e) Acoustic Observatories 
f) National Reference Stations (Coordination and Analysis)
g) Satellite Ocean Colour Calibration

The regional moorings will monitor the interaction between boundary currents and shelf water masses and their consequent impact upon ocean productivity (e.g. Perth Canyon Upwelling; Kangaroo Island Upwelling) and ecosystem distribution and resilience (e.g. Coral Sea interaction with the Great Barrier Reef ). Operation of the network will be distributed between several operators and coordinated nationally.

Passive acoustic listening station arrays will be co-located at three mooring sites. These stations will provide baseline data on ambient oceanic noise, detection of fish and mammal vocalizations and detection of underwater events. Nine National Reference Stations will increase the number of long term time series observations both in terms of variables recorded, temporal distribution and geographical extent. The satellite ocean colour calibration site will provide satellite operators and data users with access to reliable calibration and validation data for the coastal and ocean colour satellite mission data sets.</abstract>
        <keyword>Oceans | Salinity/density | Conductivity</keyword>
        <keyword>Oceans | Ocean Temperature | Water Temperature</keyword>
        <keyword>Oceans | Bathymetry | Water Depth</keyword>
        <keyword>Oceans | Ocean Optics | Fluorescence</keyword>
        <keyword>Oceans | Ocean Chemistry | Oxygen</keyword>
        <keyword>Oceans | Ocean Optics | Photosynthetically Active Radiation</keyword>
        <keyword>Oceans | Ocean Optics | Turbidity</keyword>
        <keyword>Oceans | Ocean Circulation | Ocean Currents</keyword>
        <keyword>Oceans | Ocean Chemistry | Nutrients</keyword>
        <keyword>Biosphere | Microbiota | Plankton</keyword>
        <keyword>Oceans | Ocean Chemistry | Carbon Dioxide</keyword>
        <keyword>Oceans | Ocean Chemistry | Inorganic Carbon</keyword>
        <keyword>Oceans | Ocean Chemistry | Alkalinity</keyword>
        <keyword>Buoys|Moored Buoys</keyword>
        <keyword>Current Meters/Profilers|Acoustic Doppler Current Profiler (ADCP)</keyword>
        <keyword>Fluorometers</keyword>
        <keyword>CTD (Conductivity-Temperature-Depth)</keyword>
        <keyword>Acoustic observing station</keyword>
        <keyword>Slope Mooring</keyword>
        <keyword>Shelf Mooring</keyword>
        <keyword>Deep-water mooring</keyword>
        <keyword>Australian National Mooring Network</keyword>
        <keyword>ANMN</keyword>
        <link title="Point of truth URL of this metadata record" href="http://mesttest.emii.org.au:80/geonetwork/srv/en/metadata.show?uuid=f9c151bd-d95b-4af6-8cb7-21c05b7b383b" name="" protocol="WWW:LINK-1.0-http--metadata-URL" type="text/html" />
        <link type="url">http://mesttest.emii.org.au:80/geonetwork/srv/en/metadata.show?uuid=f9c151bd-d95b-4af6-8cb7-21c05b7b383b</link>
        <link title="Search for any child records [in IMOS Metadata catalogue]" href="http://imosmest.emii.org.au/geonetwork/srv/en/search.external?any= f9c151bd-d95b-4af6-8cb7-21c05b7b383b" name="" protocol="" type="text/plain" />
        <link type="url">http://imosmest.emii.org.au/geonetwork/srv/en/search.external?any= f9c151bd-d95b-4af6-8cb7-21c05b7b383b</link>
        <link title="ANMN All Regional mooring" href="http://geoserverdev.emii.org.au/geoserver/wms" name="topp:anmn_regions" protocol="OGC:WMS-1.1.1-http-get-map" type="application/vnd.ogc.wms_xml" />
        <link title="ANMN All Regional mooring" href="http://localhost:8080/geonetwork/srv/en/google.kml?uuid=f9c151bd-d95b-4af6-8cb7-21c05b7b383b&amp;layers=topp:anmn_regions" name="topp:anmn_regions" type="application/vnd.google-earth.kml+xml" />
        <link type="wms">javascript:addWMSLayer([["topp:anmn_regions","http://geoserverdev.emii.org.au/geoserver/wms", "topp:anmn_regions","4554"]])</link>
        <link type="googleearth">/geonetwork/srv/en/google.kml?uuid=f9c151bd-d95b-4af6-8cb7-21c05b7b383b&amp;layers=topp:anmn_regions</link>
        <geonet:info xmlns:geonet="http://www.fao.org/geonetwork" xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
            <id xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:gml="http://www.opengis.net/gml" xmlns:gts="http://www.isotc211.org/2005/gts" xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:gmd="http://www.isotc211.org/2005/gmd">4554</id>
            <uuid xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:gml="http://www.opengis.net/gml" xmlns:gts="http://www.isotc211.org/2005/gts" xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:gmd="http://www.isotc211.org/2005/gmd">f9c151bd-d95b-4af6-8cb7-21c05b7b383b</uuid>
        </geonet:info>
    </metadata>
    <metadata>
        <title>IMOS - ANMN Passive Acoustic Listening Stations Sub-facility</title>
        <abstract>The IMOS Passive Acoustic Listening sub-facility deploys hydrophones that capture noise in the ocean.

Outputs from the marine passive acoustic observatory systems comprises data obtained from grids of sea noise loggers placed on the seabed at three locations around the southern coast of Australia. 

Loggers are deployed in arrays of four at Perth Canyon in Western Australia, Portland in Victoria and off the coast of Sydney in New South Wales.</abstract>
        <geoBox>
            <westBL />
            <eastBL />
            <southBL />
            <northBL />
        </geoBox>
        <link title="Point of truth URL of this metadata record" href="http://mesttest.emii.org.au:80/geonetwork/srv/en/metadata.show?uuid=e850651b-d65d-495b-8182-5dde35919616" name="" protocol="WWW:LINK-1.0-http--metadata-URL" type="text/html" />
        <link type="url">http://mesttest.emii.org.au:80/geonetwork/srv/en/metadata.show?uuid=e850651b-d65d-495b-8182-5dde35919616</link>
        <link title="ANMN Passive Acoustic Observatories" href="http://geoserverdev.emii.org.au/geoserver/wms" name="topp:anmn_acoustics" protocol="OGC:WMS-1.1.1-http-get-map" type="application/vnd.ogc.wms_xml" />
        <link title="ANMN Passive Acoustic Observatories" href="http://localhost:8080/geonetwork/srv/en/google.kml?uuid=e850651b-d65d-495b-8182-5dde35919616&amp;layers=topp:anmn_acoustics" name="topp:anmn_acoustics" type="application/vnd.google-earth.kml+xml" />
        <link type="wms">javascript:addWMSLayer([["topp:anmn_acoustics","http://geoserverdev.emii.org.au/geoserver/wms", "topp:anmn_acoustics","5592"]])</link>
        <link type="googleearth">/geonetwork/srv/en/google.kml?uuid=e850651b-d65d-495b-8182-5dde35919616&amp;layers=topp:anmn_acoustics</link>
        <geonet:info xmlns:geonet="http://www.fao.org/geonetwork" xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
            <id xmlns:gmd="http://www.isotc211.org/2005/gmd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:gml="http://www.opengis.net/gml" xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:gts="http://www.isotc211.org/2005/gts">5592</id>
            <uuid xmlns:gmd="http://www.isotc211.org/2005/gmd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:gml="http://www.opengis.net/gml" xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:gts="http://www.isotc211.org/2005/gts">e850651b-d65d-495b-8182-5dde35919616</uuid>
        </geonet:info>
    </metadata>
    <metadata>
        <title>IMOS - Australian Coastal Ocean Radar Network (ACORN) Facility</title>
        <abstract>The Australian Coastal Ocean Radar Network (ACORN) facility will comprise a coordinated network of HF radars delivering quality assured data into a national archive. 

Based on experience in Europe and the USA, deployment of these radars is expected to make a profound change to coastal ocean research in Australia. HF radar provides unprecedented time-resolved surface current maps over the monitoring sites for physical and biological ocean research. 

Deployment of the radars will be in support of regional nodes where there is a range of identified questions concerned with boundary currents and associated eddies and their interactions with shelf water and topography. In turn these are linked to productivity, connectivity of biological populations and phenomena such as coral bleaching and diseases. It will provide a basis for applied research in wave prediction and will offer test sites for hydrodynamic modelling. 

The equipment will comprise Long Range WERA and Medium Range WERA Systems and Long range CODAR and Medium-Range CODAR Systems and associated spares and transport infrastructure. An existing system being installed by James Cook University in the Capricorn/Bunker region around Heron Island on the Great Barrier Reef will be integrated into the network. A proposed HF radar acquisition by a consortium led by South Australian Research and Development Institute in South Australia will also be integrated into the network.</abstract>
        <keyword>Oceans | Ocean Circulation | Ocean Currents</keyword>
        <keyword>Oceans | Ocean Circulation | Eddies</keyword>
        <keyword>Oceans | Ocean Circulation | Upwelling</keyword>
        <keyword>Oceans | Ocean Circulation | Wind-driven Circulation</keyword>
        <keyword>Oceans | Ocean Waves | Significant Wave Height</keyword>
        <keyword>Oceans | Ocean Waves | Wave Frequency</keyword>
        <keyword>Oceans | Ocean Waves | Wave Length</keyword>
        <keyword>Oceans | Ocean Waves | Wave Period</keyword>
        <keyword>Oceans | Ocean Waves | Wave Spectra</keyword>
        <keyword>Oceans | Ocean Waves | Wave Speed/direction</keyword>
        <keyword>Oceans | Ocean Winds | Surface Winds</keyword>
        <keyword>Oceans | Coastal Processes | Coral Reefs</keyword>
        <keyword>HF Radar</keyword>
        <keyword>WERA HF Radar</keyword>
        <keyword>COastal raDAR (CODAR)</keyword>
        <keyword>doppler frequency</keyword>
        <keyword>SeaSonde</keyword>
        <keyword>Phased array system</keyword>
        <keyword>Direction finding system</keyword>
        <keyword>Oceanography</keyword>
        <geoBox>
            <westBL>112</westBL>
            <eastBL>154</eastBL>
            <southBL>-44</southBL>
            <northBL>-9</northBL>
        </geoBox>
        <link title="Point of truth URL of this metadata record" href="http://mesttest.emii.org.au:80/geonetwork/srv/en/metadata.show?uuid=1a69252d-38e4-4e95-8af8-ff92fb144cfe" name="" protocol="WWW:LINK-1.0-http--metadata-URL" type="text/html" />
        <link type="url">http://mesttest.emii.org.au:80/geonetwork/srv/en/metadata.show?uuid=1a69252d-38e4-4e95-8af8-ff92fb144cfe</link>
        <link title="ACORN Stations" href="http://geoserverdev.emii.org.au/geoserver/wms" name="topp:radar_stations" protocol="OGC:WMS-1.1.1-http-get-map" type="application/vnd.ogc.wms_xml" />
        <link title="ACORN Stations" href="http://localhost:8080/geonetwork/srv/en/google.kml?uuid=1a69252d-38e4-4e95-8af8-ff92fb144cfe&amp;layers=topp:radar_stations" name="topp:radar_stations" type="application/vnd.google-earth.kml+xml" />
        <link type="wms">javascript:addWMSLayer([["topp:radar_stations","http://geoserverdev.emii.org.au/geoserver/wms", "topp:radar_stations","135"]])</link>
        <link type="googleearth">/geonetwork/srv/en/google.kml?uuid=1a69252d-38e4-4e95-8af8-ff92fb144cfe&amp;layers=topp:radar_stations</link>
        <geonet:info xmlns:geonet="http://www.fao.org/geonetwork" xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
            <id xmlns:gmd="http://www.isotc211.org/2005/gmd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:gml="http://www.opengis.net/gml" xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:gts="http://www.isotc211.org/2005/gts">135</id>
            <uuid xmlns:gmd="http://www.isotc211.org/2005/gmd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:gml="http://www.opengis.net/gml" xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:gts="http://www.isotc211.org/2005/gts">1a69252d-38e4-4e95-8af8-ff92fb144cfe</uuid>
        </geonet:info>
    </metadata>
    <metadata>
        <title>IMOS - Satellite Remote Sensing (SRS) facility</title>
        <abstract>The aim of the Satellite Remote Sensing (SRS) facility is to provide access to a range of satellite derived marine data products covering the Australian region.

The SRS has provided funds to establish a new X-Band reception facility at the Australian Institute of Marine Research near Townsville and upgrade the Tasmanian Earth Resource Satellite Station near Hobart.  

These stations and facilities in Perth, Melbourne, Alice Springs and Darwin form a network supplying the SRS with near real-time data.  These data are combined and processed to a number of products which are placed on disk storage systems in Melbourne, Canberra and Perth.

The Bureau of Meteorology has developed a sea surface temperature (SST) product in GHRSST-PP (www.ghrsst-pp.org)  L3P format. The Bureau is developing some other SST products including daily and skin SSTs.
These new products and some ¿ocean colour¿ products from MODIS will  gradually become available.

Scientific users can access these data products are available through OPeNDAP and THREDDS supported technology and also through interfaces provided by eMII and the SRS (www.imos.org.au and www.imos.org.au/srs).</abstract>
        <keyword>Oceans | Ocean Temperature | Water Temperature</keyword>
        <keyword>Imaging Radar Systems</keyword>
        <link title="Point of truth URL of this metadata record" href="http://mesttest.emii.org.au:80/geonetwork/srv/en/metadata.show?uuid=744ac2a9-689c-40d3-b262-0df6863f0327" name="" protocol="WWW:LINK-1.0-http--metadata-URL" type="text/html" />
        <link type="url">http://mesttest.emii.org.au:80/geonetwork/srv/en/metadata.show?uuid=744ac2a9-689c-40d3-b262-0df6863f0327</link>
        <link title="SRS Satellite" href="http://geoserverdev.emii.org.au/geoserver/wms" name="topp:satellite" protocol="OGC:WMS-1.1.1-http-get-map" type="application/vnd.ogc.wms_xml" />
        <link title="SRS Satellite" href="http://localhost:8080/geonetwork/srv/en/google.kml?uuid=744ac2a9-689c-40d3-b262-0df6863f0327&amp;layers=topp:satellite" name="topp:satellite" type="application/vnd.google-earth.kml+xml" />
        <link type="wms">javascript:addWMSLayer([["topp:satellite","http://geoserverdev.emii.org.au/geoserver/wms", "topp:satellite","155"]])</link>
        <link type="googleearth">/geonetwork/srv/en/google.kml?uuid=744ac2a9-689c-40d3-b262-0df6863f0327&amp;layers=topp:satellite</link>
        <geonet:info xmlns:geonet="http://www.fao.org/geonetwork" xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
            <id xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:gml="http://www.opengis.net/gml" xmlns:gts="http://www.isotc211.org/2005/gts" xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:gmd="http://www.isotc211.org/2005/gmd">155</id>
            <uuid xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:gml="http://www.opengis.net/gml" xmlns:gts="http://www.isotc211.org/2005/gts" xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:gmd="http://www.isotc211.org/2005/gmd">744ac2a9-689c-40d3-b262-0df6863f0327</uuid>
        </geonet:info>
    </metadata>
    <metadata>
        <title>IMOS SOOP-TMV - Data collected on the Spirit of Tasmania I during transect between Melbourne and Devonport.</title>
        <abstract>Enhancement of Measurements on Ships of Opportunity (SOOP)- Temperate Merchant Vessel TMV collects data from an autonomous marine monitoring system on the Spirit of Tasmania I to monitor long-term climate change indicators and measures of ecosystem health in Bass Strait surface waters and coastal waters of Victoria and Tasmania.

TMV maintains an underway sampling Seabird SBE45 thermo-salinograph (TSG), a Wetlabs combination chlorophyll fluorometer and turbidity sensor (FLNTU), a GPS were deployed in June 2008 on the Spirit of Tasmania I (VLST) for initial trials. Routine data collection commenced on 28 August 2008. 

Transects are dependent upon Spirit of Tasmania 1 operating schedule.  These are typically undertaken 7:30pm to 7am with subsequent in-port monitoring 7am-7:30pm at Port Melbourne and Devonport docks. During summer and holiday periods the vessel undertakes continuous transects with 2-3 hour turnaround at the ports.

Transect data files are available through the IMOS OPeNDAP server. Graphic representation of the temperature, salinity, turbidity, chlorophyll-a by distance are also available.</abstract>
        <keyword>Oceans | Ocean Temperature | Sea Surface Temperature</keyword>
        <keyword>Oceans | Salinity/density | Salinity</keyword>
        <keyword>Oceans | Ocean Optics | Fluorescence</keyword>
        <keyword>Oceans | Ocean Optics | Turbidity</keyword>
        <geoBox>
            <westBL>144.6158333</westBL>
            <eastBL>146.3655556</eastBL>
            <southBL>-41.180277778</southBL>
            <northBL>-37.84416667</northBL>
        </geoBox>
        <link title="Point of truth URL of this metadata record" href="http://mesttest.emii.org.au:80/geonetwork/srv/en/metadata.show?uuid=02640f4e-08d0-4f3a-956b-7f9b58966ccc" name="" protocol="WWW:LINK-1.0-http--metadata-URL" type="text/html" />
        <link type="url">http://mesttest.emii.org.au:80/geonetwork/srv/en/metadata.show?uuid=02640f4e-08d0-4f3a-956b-7f9b58966ccc</link>
        <link title="Data available via the IMOS OPeNDAP server" href="http://opendap-tpac.arcs.org.au/thredds/catalog/IMOS/SOOP/SOOP-TMV/VLST_Spirit-of-Tasmania-1/transect/catalog.html" name="" protocol="WWW:LINK-1.0-http--link" type="text/html" />
        <link type="url">http://opendap-tpac.arcs.org.au/thredds/catalog/IMOS/SOOP/SOOP-TMV/VLST_Spirit-of-Tasmania-1/transect/catalog.html</link>
        <link title="IMOS SOOP-TMV ship operations log (August to December 2008)" href="http://mesttest.emii.org.au:80/geonetwork/srv/en/file.disclaimer?id=4519&amp;fname=IMOS_SOOP_TMV_ship-operations-log_August-December-2008.doc&amp;access=private" name="IMOS_SOOP_TMV_ship-operations-log_August-December-2008.doc" protocol="WWW:DOWNLOAD-1.0-http--downloaddata" type="application/msword" />
        <link type="download">http://mesttest.emii.org.au:80/geonetwork/srv/en/file.disclaimer?id=4519&amp;fname=IMOS_SOOP_TMV_ship-operations-log_August-December-2008.doc&amp;access=private</link>
        <link title="SOOP Temperate Merchant Vessel" href="http://geoserverdev.emii.org.au/geoserver/wms" name="topp:soop_tmv" protocol="OGC:WMS-1.1.1-http-get-map" type="application/vnd.ogc.wms_xml" />
        <link title="SOOP Temperate Merchant Vessel" href="http://localhost:8080/geonetwork/srv/en/google.kml?uuid=02640f4e-08d0-4f3a-956b-7f9b58966ccc&amp;layers=topp:soop_tmv" name="topp:soop_tmv" type="application/vnd.google-earth.kml+xml" />
        <link type="wms">javascript:addWMSLayer([["topp:soop_tmv","http://geoserverdev.emii.org.au/geoserver/wms", "topp:soop_tmv","4519"]])</link>
        <link type="googleearth">/geonetwork/srv/en/google.kml?uuid=02640f4e-08d0-4f3a-956b-7f9b58966ccc&amp;layers=topp:soop_tmv</link>
        <link title="SOOP TMV (last 20 hours)" href="http://geoserverdev.emii.org.au/geoserver/wms" name="topp:soop_tmv_recent" protocol="OGC:WMS-1.1.1-http-get-map" type="application/vnd.ogc.wms_xml" />
        <link title="SOOP TMV (last 20 hours)" href="http://localhost:8080/geonetwork/srv/en/google.kml?uuid=02640f4e-08d0-4f3a-956b-7f9b58966ccc&amp;layers=topp:soop_tmv_recent" name="topp:soop_tmv_recent" type="application/vnd.google-earth.kml+xml" />
        <link type="wms">javascript:addWMSLayer([["topp:soop_tmv_recent","http://geoserverdev.emii.org.au/geoserver/wms", "topp:soop_tmv_recent","4519"]])</link>
        <link type="googleearth">/geonetwork/srv/en/google.kml?uuid=02640f4e-08d0-4f3a-956b-7f9b58966ccc&amp;layers=topp:soop_tmv_recent</link>
        <geonet:info xmlns:geonet="http://www.fao.org/geonetwork" xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
            <id xmlns:gmd="http://www.isotc211.org/2005/gmd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:gml="http://www.opengis.net/gml" xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:gts="http://www.isotc211.org/2005/gts">4519</id>
            <uuid xmlns:gmd="http://www.isotc211.org/2005/gmd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:gml="http://www.opengis.net/gml" xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:gts="http://www.isotc211.org/2005/gts">02640f4e-08d0-4f3a-956b-7f9b58966ccc</uuid>
        </geonet:info>
    </metadata>
    <metadata>
        <title>IMOS - Australian National Facility for Ocean Gliders (ANFOG)</title>
        <abstract>The Australian National Facility for Ocean Gliders (ANFOG), with IMOS/NCRIS funding, deploys a fleet of eight gliders around Australia.

The underwater ocean glider represents a technological revolution for oceanography. Autonomous ocean gliders can be built relatively cheaply, are controlled remotely and are reusables allowing them to make repeated subsurface ocean observations at a fraction of the cost of conventional methods. The data retrieved from the glider fleet will contribute to the study of the major boundary current systems surrounding Australia and their links to coastal ecosystems.

The ANFOG glider fleet consists of two types; Slocum gliders and Seagliders. 
Slocum gliders (named for Joshua Slocum, the first solo global circumnavigator), manufactured by Webb Research Corp are optimised for shallow coastal waters (&lt;200m) were high manoeuvrability is needed. ANFOG will have three Slocum gliders for deployment on the continental shelf. Seagliders, built at the University of Washington, are designed to operate more efficiently in the open ocean up to 1000m water depth. ANFOG uses their five Seagliders to monitor the boundary currents and continental shelves, which is valuable for gathering long-term environmental records of physical, chemical and biological data not widely measured to date. Whilst the Slocum gliders, due to their low cost and operational flexibility, will be of great use in intensive coastal monitoring, both types of gliders weigh only 52kg, enabling them to be launched from small boats. They have a suite of sensors able to record temperature, salinity, dissolved oxygen, turbidity, dissolved organic matter and chlorophyll against position and depth

Sustained ocean observations will allow researchers to document the natural variability of the ocean, and better understand the effect of climate change on coastal ecosystems. The IMOS gliders will focus particularly on the major boundary currents that run down the Australia coast, the Leeuwin in the west and the East Australian Current (EAC). The study of these currents is critical for understanding the north-south transport of freshwater, heat and biogeochemical properties. The currents exert a large influence on coastal ecosystems, shipping lanes and fisheries.

Following a public call for proposals in 2007 for the IMOS infrastructure, the ocean gliders have been allocated to four projects which will be coordinated by research teams at four of the IMOS Science Nodes:
-NSW-IMOS (Exploring hydrodgraphy and fluorescence in the East Australian Current, its eddy field and in the Tasman front) 
-Bluewater (Monitoring the Southward extension of the East Australian Current)
-SAIMOS (Boundary and shelf currents exchange with the shelves of South Australia and Victoria)
-WAIMOS (Understand the role of the Leeuwin current system in controlling not only the marine life but also the climate of south western Australia)</abstract>
        <keyword>Oceans | Salinity/density | Conductivity</keyword>
        <keyword>Oceans | Ocean Temperature | Water Temperature</keyword>
        <keyword>Oceans | Bathymetry | Water Depth</keyword>
        <keyword>Oceans | Ocean Optics | Fluorescence</keyword>
        <keyword>Oceans | Ocean Chemistry | Oxygen</keyword>
        <keyword>Oceans | Ocean Optics | Turbidity</keyword>
        <keyword>Oceans | Ocean Circulation | Ocean Currents</keyword>
        <keyword>Oceans | Ocean Chemistry | Chlorophyll</keyword>
        <keyword>Oceans | Salinity/density | Salinity</keyword>
        <keyword>Fluorometers</keyword>
        <keyword>CTD (Conductivity-Temperature-Depth)</keyword>
        <keyword>Earth Sciences | Oceanography | Physical Oceanography</keyword>
        <keyword>IMOS Nodes | Western Australia (WA)</keyword>
        <keyword>IMOS Nodes | WAIMOS</keyword>
        <keyword>IMOS Nodes | WA-IMOS</keyword>
        <keyword>IMOS Nodes | Bluewater</keyword>
        <keyword>Global/Oceans | Indian Ocean</keyword>
        <keyword>Global/Oceans | Southern Ocean</keyword>
        <keyword>Regional Seas | Tasman Sea</keyword>
        <keyword>Marine Features (Australia) | Great Australian Bight, SA/WA</keyword>
        <geoBox>
            <westBL>112</westBL>
            <eastBL>154</eastBL>
            <southBL>-44</southBL>
            <northBL>-9</northBL>
        </geoBox>
        <link title="Point of truth URL of this metadata record" href="http://mesttest.emii.org.au:80/geonetwork/srv/en/metadata.show?uuid=11b3ccd0-d9e0-11dc-8635-00188b4c0af8" name="" protocol="WWW:LINK-1.0-http--metadata-URL" type="text/html" />
        <link type="url">http://mesttest.emii.org.au:80/geonetwork/srv/en/metadata.show?uuid=11b3ccd0-d9e0-11dc-8635-00188b4c0af8</link>
        <link title="ANFOG data management document" href="http://mesttest.emii.org.au:80/geonetwork/srv/en/file.disclaimer?id=99&amp;fname=ANFOG_data_management2_3.doc&amp;access=private" name="ANFOG_data_management2_3.doc" protocol="WWW:DOWNLOAD-1.0-http--download" type="application/msword" />
        <link type="download">http://mesttest.emii.org.au:80/geonetwork/srv/en/file.disclaimer?id=99&amp;fname=ANFOG_data_management2_3.doc&amp;access=private</link>
        <link title="ANFOG All deployments" href="http://geoserverdev.emii.org.au/geoserver/wms" name="topp:anfog_glider" protocol="OGC:WMS-1.1.1-http-get-map" type="application/vnd.ogc.wms_xml" />
        <link title="ANFOG All deployments" href="http://localhost:8080/geonetwork/srv/en/google.kml?uuid=11b3ccd0-d9e0-11dc-8635-00188b4c0af8&amp;layers=topp:anfog_glider" name="topp:anfog_glider" type="application/vnd.google-earth.kml+xml" />
        <link type="wms">javascript:addWMSLayer([["topp:anfog_glider","http://geoserverdev.emii.org.au/geoserver/wms", "topp:anfog_glider","99"]])</link>
        <link type="googleearth">/geonetwork/srv/en/google.kml?uuid=11b3ccd0-d9e0-11dc-8635-00188b4c0af8&amp;layers=topp:anfog_glider</link>
        <geonet:info xmlns:geonet="http://www.fao.org/geonetwork" xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
            <id xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:gml="http://www.opengis.net/gml" xmlns:gts="http://www.isotc211.org/2005/gts" xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:gmd="http://www.isotc211.org/2005/gmd">99</id>
            <uuid xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:gml="http://www.opengis.net/gml" xmlns:gts="http://www.isotc211.org/2005/gts" xmlns:gco="http://www.isotc211.org/2005/gco" xmlns:gmd="http://www.isotc211.org/2005/gmd">11b3ccd0-d9e0-11dc-8635-00188b4c0af8</uuid>
        </geonet:info>
    </metadata>
</response>
"""
	
	def geoNetworkResponse

	protected void setUp() {
        super.setUp()
		def config = new ConfigObject()
		config.geonetwork.feature.type.indentifier.regex = 'topp:'
		config.geonetwork.link.protocol.regex = 'OGC:WMS-1\\.(1\\.1|3\\.0)-http-get-map'
		GrailsApplication.metaClass.getConfig = {-> config }
		geoNetworkResponse = new GeoNetworkResponse(config, responseXml)
    }

    protected void tearDown() {
        super.tearDown()
    }

	void testUpdateSummaryCounts() {
		def five = 5
		geoNetworkResponse._updateSummaryCount(five)
		geoNetworkResponse._updateHitsUsedForSummaryCount(five)
		
		assertEquals five, geoNetworkResponse.tree.summary[0].@count.toInteger()
		assertEquals five, geoNetworkResponse.tree.summary[0].@hitsusedforsummary.toInteger()
		
		geoNetworkResponse._decrementSummaryCounts()
		assertEquals 4, geoNetworkResponse.tree.summary[0].@count.toInteger()
		assertEquals 4, geoNetworkResponse.tree.summary[0].@hitsusedforsummary.toInteger()
		
	}
	
	void testUpdateKeywordCounts() {
		_assertKeywordCountChecks(['Oceans | Salinity/density | Conductivity': 9, 'Earth Sciences | Oceanography | Physical Oceanography': 12, 'Regional Seas | Tasman Sea': 5])
	}
	
	void testUpdateMoreThanTenKeywordCounts() {
		_assertKeywordCountChecks(
			[
				'Oceans | Salinity/density | Conductivity': 9, 
				'Earth Sciences | Oceanography | Physical Oceanography': 12, 
				'Regional Seas | Tasman Sea': 5,
				'Oceans | Ocean Temperature | Water Temperature': 6,
				'Oceans | Bathymetry | Water Depth': 8,
				'Oceans | Ocean Optics | Fluorescence': 18,
				'Oceans | Ocean Chemistry | Oxygen': 123,
				'Oceans | Ocean Optics | Turbidity': 34,
				'Oceans | Ocean Circulation | Ocean Currents': 3,
				'Oceans | Ocean Chemistry | Chlorophyll': 4,
				'Oceans | Salinity/density | Salinity': 89,
				'Fluorometers': 13,
				'CTD (Conductivity-Temperature-Depth)': 126
			]
		)
	}
	
	void testUpdateEmptyKeywordCounts() {
		_assertKeywordCountChecks([:])
	}

	def _assertKeywordCountChecks(keywords) {
		geoNetworkResponse.keywordCounts = keywords
		geoNetworkResponse._updateKeywordCounts()

		def sorted = keywords.entrySet().sort{ it.value }.reverse()
		if (sorted.size() > 10) {
			sorted = sorted[0..9]
		}
		
		assertEquals sorted.size(), geoNetworkResponse.tree.summary.keywords.keyword.size()
		sorted.each { entry ->
			assertEquals entry.value, geoNetworkResponse.tree.summary.keywords.keyword.find{ it.@name == entry.key}.@count.toInteger()
		}
	}
	
	void testParseXmlToGeonetworkMetadataObjects() {
		def map = geoNetworkResponse._parseXmlToGeonetworkMetadataObjects()
		assertTrue map.size() > 0
	}
	
	void testGetGeonetworkMetadataObjects() {
		def map = geoNetworkResponse._parseXmlToGeonetworkMetadataObjects()
		def col = geoNetworkResponse.getGeonetworkMetadataObjects()
		
		assertTrue col.size() == map.size()
	}
	
	void testCollectUuids() {
		def col = geoNetworkResponse.getGeonetworkMetadataObjects()
		assertTrue geoNetworkResponse.uuids.collect { it != null }.size() > 0
	}
	
	void testCollectKeywordCounts() {
		def col = geoNetworkResponse.getGeonetworkMetadataObjects()
		assertTrue geoNetworkResponse.keywordCounts.size() > 0
	}
	
	void testNullXml() {
		def config = new ConfigObject()
		config.geonetwork.feature.type.indentifier.regex = 'topp:'
		config.geonetwork.link.protocol.regex = 'OGC:WMS-1\\.(1\\.1|3\\.0)-http-get-map'
		GrailsApplication.metaClass.getConfig = {-> config }
		
		shouldFail(NullPointerException) {
			def response = new GeoNetworkResponse(config, null)
		}
	}
	
	void testNoChangeBuildResponseXml() {
		def expected = new XmlSlurper().parseText(responseXml)
		def result = new XmlSlurper().parseText(geoNetworkResponse._buildResponseXml())
		
		assertEquals result.summary.@count.toInteger(), expected.summary.@count.toInteger()
	}
	
	void testSpatialResponse() {
		def keywords = [
			'Oceans | Salinity/density | Conductivity',
			'Oceans | Ocean Temperature | Water Temperature',
			'Oceans | Bathymetry | Water Depth',
			'Oceans | Ocean Optics | Fluorescence',
			'Oceans | Ocean Chemistry | Oxygen',
			'Oceans | Ocean Optics | Turbidity',
			'Oceans | Ocean Circulation | Ocean Currents',
			'Oceans | Ocean Chemistry | Chlorophyll',
			'Oceans | Salinity/density | Salinity',
			'Fluorometers',
			'CTD (Conductivity-Temperature-Depth)',
			'Earth Sciences | Oceanography | Physical Oceanography',
			'IMOS Nodes | Western Australia (WA)',
			'IMOS Nodes | WAIMOS',
			'IMOS Nodes | WA-IMOS',
			'IMOS Nodes | Bluewater',
			'Global/Oceans | Indian Ocean',
			'Global/Oceans | Southern Ocean',
			'Regional Seas | Tasman Sea',
			'Marine Features (Australia) | Great Australian Bight, SA/WA'
		]
		
		mockDomain(FeatureType)
		def featureType = new FeatureType()
		featureType.geonetworkUuid = '02640f4e-08d0-4f3a-956b-7f9b58966ccc'
		featureType.featureTypeName = 'topp:anfog_glider'
		featureType.featureTypeId = '1'
		
		def metadata = geoNetworkResponse.getGeonetworkMetadataObjects()
		
		def result = geoNetworkResponse.getSpatialResponse(metadata, [featureType])
		def xml = new XmlSlurper().parseText(result)
		
		assertTrue 37 > xml.summary.@count.toInteger()
		
		xml.summary.keywords.keyword.each { keyword ->
			// This assert makes sure that a node hasn't been written as a string
			assertFalse keyword.@name.toString().contains("keyword[attributes")
		}
	}
	
	void testFromAndTo() {
		assertEquals 1, geoNetworkResponse.from
		assertEquals 9, geoNetworkResponse.to
	}
	
	void testEmptyResponse() {
		def metadata = geoNetworkResponse.getGeonetworkMetadataObjects()
		def result = geoNetworkResponse.getSpatialResponse(metadata, [])
		assertEquals geoNetworkResponse._emptyResponse(), result
		
		result = geoNetworkResponse.getSpatialResponse(metadata, null)
		assertEquals geoNetworkResponse._emptyResponse(), result
	}
	
	void testCount() {
		assertEquals 37, geoNetworkResponse.count
	}
	
	void testProtocolRegex() {
		assertTrue geoNetworkResponse._isProtocol('OGC:WMS-1.1.1-http-get-map')
		assertTrue geoNetworkResponse._isProtocol('OGC:WMS-1.3.0-http-get-map')
	}
	
	void testFeatureTypeLinkRegex() {
		assertTrue geoNetworkResponse._isFeatureTypeLink('OGC:WMS-1.1.1-http-get-map')
	}
	
	void testParseUuid() {
		def tree = geoNetworkResponse.tree
		def uuid = geoNetworkResponse._parseUuid(tree.metadata[0])
		assertEquals 'd26c4828-5cfc-4872-b998-ffd44f18cd31', uuid
	}
	
	void testCountOfGetGeonetworkMetadataObjects() {
		def metadataList = geoNetworkResponse.getGeonetworkMetadataObjects()
		assertEquals 11, metadataList.size()
	}
	
	void testResponseWhitespaceRemoval() {
		def result = geoNetworkResponse._buildResponseXml()
		assertFalse true && result =~ />\s/
	}
}

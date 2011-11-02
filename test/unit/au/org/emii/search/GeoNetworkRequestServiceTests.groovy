package au.org.emii.search

import grails.test.*

class GeoNetworkRequestServiceTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testUuidCollection() {
		def xml = """<?xml version="1.0" encoding="UTF-8"?>
<response from="1" to="35" selected="0">
  <summary count="35" type="local" hitsusedforsummary="35">
    <keywords>
      <keyword count="28" name="SHIPS |"/>
      <keyword count="28" name="ECHO SOUNDERS |"/>
      <keyword count="28" name="ATMOSPHERE | ATMOSPHERIC TEMPERATURE | AIR TEMPERATURE |  |"/>
      <keyword count="28" name="Oceans | Ocean Temperature | Water Temperature"/>
      <keyword count="28" name="AMD"/>
      <keyword count="28" name="ATMOSPHERE | ATMOSPHERIC PRESSURE | SEA LEVEL PRESSURE |  |"/>
      <keyword count="28" name="ATMOSPHERE | ATMOSPHERIC RADIATION | SOLAR RADIATION |  |"/>
      <keyword count="28" name="ATMOSPHERE | ATMOSPHERIC WATER VAPOR | HUMIDITY |  |"/>
      <keyword count="28" name="METEOROLOGY"/>
      <keyword count="28" name="MARINE"/>
    </keywords>
  </summary>
  <metadata xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
    <metadatacreationdate/>
    <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
      <id>4485</id>
      <uuid>3999f117-b50e-4b5e-92e9-82ed04a736d5</uuid>
      <schema>iso19139.mcp-1.4</schema>
      <createDate>2008-12-04T14:38:07</createDate>
      <changeDate>2010-10-08T12:53:21</changeDate>
      <source>2e5b03a0-fbd5-4215-a650-7d121ffb6635</source>
      <selected>false</selected>
      <category internal="true">dataset</category>
    </geonet:info>
    <revisiondate/>
  </metadata>
  <metadata xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
    <metadatacreationdate/>
    <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
      <id>4537</id>
      <uuid>fd1b7df5-7b5b-4669-9f07-302804bae527</uuid>
      <schema>iso19139.mcp-1.4</schema>
      <createDate>2009-02-24T15:48:58</createDate>
      <changeDate>2010-10-08T12:49:37</changeDate>
      <source>2e5b03a0-fbd5-4215-a650-7d121ffb6635</source>
      <selected>false</selected>
      <category internal="true">dataset</category>
    </geonet:info>
    <revisiondate/>
  </metadata>
  <metadata xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
    <metadatacreationdate/>
    <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
      <id>135</id>
      <uuid>1a69252d-38e4-4e95-8af8-ff92fb144cfe</uuid>
      <schema>iso19139.mcp-1.4</schema>
      <createDate>2008-05-07T12:33:45</createDate>
      <changeDate>2010-10-08T13:21:44</changeDate>
      <source>2e5b03a0-fbd5-4215-a650-7d121ffb6635</source>
      <selected>false</selected>
      <category internal="true">dataset</category>
    </geonet:info>
    <revisiondate/>
  </metadata>
  <metadata xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
    <metadatacreationdate/>
    <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
      <id>8128</id>
      <uuid>4637bd9b-8fba-4a10-bf23-26a511e17042</uuid>
      <schema>iso19139.mcp-1.4</schema>
      <createDate>2010-06-04T17:03:13</createDate>
      <changeDate>2010-10-08T13:23:11</changeDate>
      <source>2e5b03a0-fbd5-4215-a650-7d121ffb6635</source>
      <selected>false</selected>
      <category internal="true">dataset</category>
    </geonet:info>
    <revisiondate/>
  </metadata>
  <metadata xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
    <metadatacreationdate/>
    <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
      <id>155</id>
      <uuid>744ac2a9-689c-40d3-b262-0df6863f0327</uuid>
      <schema>iso19139.mcp-1.4</schema>
      <createDate>2008-05-12T10:30:33</createDate>
      <changeDate>2010-10-08T13:24:51</changeDate>
      <source>2e5b03a0-fbd5-4215-a650-7d121ffb6635</source>
      <selected>false</selected>
      <category internal="true">dataset</category>
    </geonet:info>
    <revisiondate/>
  </metadata>
  <metadata xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
    <metadatacreationdate/>
    <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
      <id>5592</id>
      <uuid>e850651b-d65d-495b-8182-5dde35919616</uuid>
      <schema>iso19139.mcp-1.4</schema>
      <createDate>2009-06-25T15:37:02</createDate>
      <changeDate>2010-10-08T13:19:47</changeDate>
      <source>2e5b03a0-fbd5-4215-a650-7d121ffb6635</source>
      <selected>false</selected>
      <category internal="true">dataset</category>
    </geonet:info>
    <revisiondate/>
  </metadata>
  <metadata xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
    <metadatacreationdate/>
    <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
      <id>4554</id>
      <uuid>f9c151bd-d95b-4af6-8cb7-21c05b7b383b</uuid>
      <schema>iso19139.mcp-1.4</schema>
      <createDate>2009-03-10T17:30:44</createDate>
      <changeDate>2010-10-08T13:18:41</changeDate>
      <source>2e5b03a0-fbd5-4215-a650-7d121ffb6635</source>
      <selected>false</selected>
      <category internal="true">dataset</category>
    </geonet:info>
    <revisiondate/>
  </metadata>
  <metadata xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
    <metadatacreationdate/>
    <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
      <id>4519</id>
      <uuid>02640f4e-08d0-4f3a-956b-7f9b58966ccc</uuid>
      <schema>iso19139.mcp-1.4</schema>
      <createDate>2008-12-22T13:37:36</createDate>
      <changeDate>2010-10-08T12:51:28</changeDate>
      <source>2e5b03a0-fbd5-4215-a650-7d121ffb6635</source>
      <selected>false</selected>
      <category internal="true">dataset</category>
    </geonet:info>
    <revisiondate/>
  </metadata>
  <metadata xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
    <metadatacreationdate/>
    <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
      <id>4564</id>
      <uuid>0d9c8283-6aca-4c21-8495-deed7f316c75</uuid>
      <schema>iso19139.mcp-1.4</schema>
      <createDate>2009-04-17T13:50:07</createDate>
      <changeDate>2010-10-08T12:08:03</changeDate>
      <source>2e5b03a0-fbd5-4215-a650-7d121ffb6635</source>
      <selected>false</selected>
      <category internal="true">dataset</category>
    </geonet:info>
    <revisiondate/>
  </metadata>
  <metadata xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
    <metadatacreationdate/>
    <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
      <id>99</id>
      <uuid>11b3ccd0-d9e0-11dc-8635-00188b4c0af8</uuid>
      <schema>iso19139.mcp-1.4</schema>
      <createDate>2008-05-06T23:36:15</createDate>
      <changeDate>2010-10-08T13:05:04</changeDate>
      <source>2e5b03a0-fbd5-4215-a650-7d121ffb6635</source>
      <selected>false</selected>
      <category internal="true">dataset</category>
    </geonet:info>
    <revisiondate/>
  </metadata>
  <metadata xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
    <metadatacreationdate/>
    <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
      <id>4613</id>
      <uuid>190c2c53-5d0f-46be-b2ed-fd08313386e5</uuid>
      <schema>iso19139.mcp-1.4</schema>
      <createDate>2009-06-05T09:36:50</createDate>
      <changeDate>2010-10-08T12:20:30</changeDate>
      <source>2e5b03a0-fbd5-4215-a650-7d121ffb6635</source>
      <selected>false</selected>
      <category internal="true">dataset</category>
    </geonet:info>
    <revisiondate/>
  </metadata>
  <metadata xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
    <metadatacreationdate/>
    <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
      <id>136</id>
      <uuid>4260aa0a-0d0a-4dd0-9ebc-74d2bf937e21</uuid>
      <schema>iso19139.mcp-1.4</schema>
      <createDate>2008-05-07T12:45:35</createDate>
      <changeDate>2010-10-08T13:24:07</changeDate>
      <source>2e5b03a0-fbd5-4215-a650-7d121ffb6635</source>
      <selected>false</selected>
      <category internal="true">dataset</category>
    </geonet:info>
    <revisiondate/>
  </metadata>
  <metadata xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
    <metadatacreationdate/>
    <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
      <id>6810</id>
      <uuid>6af5cbb3-ee22-4444-a120-8f13e6c3f204</uuid>
      <schema>iso19139.mcp-1.4</schema>
      <createDate>2010-01-19T15:46:11</createDate>
      <changeDate>2010-10-08T12:57:56</changeDate>
      <source>2e5b03a0-fbd5-4215-a650-7d121ffb6635</source>
      <selected>false</selected>
      <category internal="true">dataset</category>
    </geonet:info>
    <revisiondate/>
  </metadata>
  <metadata xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
    <metadatacreationdate/>
    <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
      <id>4626</id>
      <uuid>6c981d98-d7fb-4120-9ebe-347ef1188ae0</uuid>
      <schema>iso19139.mcp-1.4</schema>
      <createDate>2009-06-10T12:34:36</createDate>
      <changeDate>2010-10-08T13:20:37</changeDate>
      <source>2e5b03a0-fbd5-4215-a650-7d121ffb6635</source>
      <selected>false</selected>
      <category internal="true">dataset</category>
    </geonet:info>
    <revisiondate/>
  </metadata>
  <metadata xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
    <metadatacreationdate/>
    <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
      <id>270956</id>
      <uuid>735b3fea-a5f6-48bf-9b8c-744a940d09b3</uuid>
      <schema>iso19139.mcp-1.4</schema>
      <createDate>2010-10-15T21:12:02</createDate>
      <changeDate>2010-10-15T21:17:49</changeDate>
      <source>2e5b03a0-fbd5-4215-a650-7d121ffb6635</source>
      <selected>false</selected>
      <category internal="true">dataset</category>
    </geonet:info>
    <revisiondate/>
  </metadata>
  <metadata xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
    <metadatacreationdate/>
    <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
      <id>4625</id>
      <uuid>8cc13f98-9897-4193-8ba6-d1f05356d3f2</uuid>
      <schema>iso19139.mcp-1.4</schema>
      <createDate>2009-06-09T11:19:04</createDate>
      <changeDate>2010-10-08T12:26:06</changeDate>
      <source>2e5b03a0-fbd5-4215-a650-7d121ffb6635</source>
      <selected>false</selected>
      <category internal="true">dataset</category>
    </geonet:info>
    <revisiondate/>
  </metadata>
  <metadata xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
    <metadatacreationdate/>
    <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
      <id>8067</id>
      <uuid>a789bcd5-94c9-4efd-ac21-4257687cbb55</uuid>
      <schema>iso19139.mcp-1.4</schema>
      <createDate>2010-05-13T10:30:50</createDate>
      <changeDate>2010-10-08T13:03:56</changeDate>
      <source>2e5b03a0-fbd5-4215-a650-7d121ffb6635</source>
      <selected>false</selected>
      <category internal="true">dataset</category>
    </geonet:info>
    <revisiondate/>
  </metadata>
  <metadata xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
    <metadatacreationdate/>
    <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
      <id>4623</id>
      <uuid>af5d0ff9-bb9c-4b7c-a63c-854a630b6984</uuid>
      <schema>iso19139.mcp-1.4</schema>
      <createDate>2009-06-09T10:09:56</createDate>
      <changeDate>2010-10-08T13:17:42</changeDate>
      <source>2e5b03a0-fbd5-4215-a650-7d121ffb6635</source>
      <selected>false</selected>
      <category internal="true">dataset</category>
    </geonet:info>
    <revisiondate/>
  </metadata>
  <metadata xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
    <metadatacreationdate/>
    <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
      <id>6815</id>
      <uuid>0d06ada4-42e1-4ef6-898f-9fc2a866054a</uuid>
      <schema>iso19139.mcp-1.4</schema>
      <createDate>2010-01-22T16:22:51</createDate>
      <changeDate>2010-10-08T13:02:09</changeDate>
      <source>2e5b03a0-fbd5-4215-a650-7d121ffb6635</source>
      <selected>false</selected>
      <category internal="true">dataset</category>
    </geonet:info>
    <revisiondate/>
  </metadata>
  <metadata xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
    <metadatacreationdate/>
    <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
      <id>4607</id>
      <uuid>63db5801-cc19-40ef-83b3-85ccba884cf7</uuid>
      <schema>iso19139.mcp-1.4</schema>
      <createDate>2009-06-01T20:43:59</createDate>
      <changeDate>2010-10-08T12:47:43</changeDate>
      <source>2e5b03a0-fbd5-4215-a650-7d121ffb6635</source>
      <selected>false</selected>
      <category internal="true">dataset</category>
    </geonet:info>
    <revisiondate/>
  </metadata>
  <metadata xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
    <metadatacreationdate/>
    <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
      <id>301279</id>
      <uuid>200910000</uuid>
      <schema>iso19139.mcp-1.4</schema>
      <createDate>2010-12-17T00:08:11</createDate>
      <changeDate>2010-12-17T00:08:11</changeDate>
      <source>2e5b03a0-fbd5-4215-a650-7d121ffb6635</source>
      <selected>false</selected>
      <category internal="true">dataset</category>
    </geonet:info>
    <revisiondate/>
  </metadata>
  <metadata xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
    <metadatacreationdate/>
    <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
      <id>301280</id>
      <uuid>200910010</uuid>
      <schema>iso19139.mcp-1.4</schema>
      <createDate>2010-12-17T00:08:50</createDate>
      <changeDate>2010-12-17T00:08:50</changeDate>
      <source>2e5b03a0-fbd5-4215-a650-7d121ffb6635</source>
      <selected>false</selected>
      <category internal="true">dataset</category>
    </geonet:info>
    <revisiondate/>
  </metadata>
  <metadata xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
    <metadatacreationdate/>
    <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
      <id>301281</id>
      <uuid>200910020</uuid>
      <schema>iso19139.mcp-1.4</schema>
      <createDate>2010-12-17T00:09:09</createDate>
      <changeDate>2010-12-17T00:09:09</changeDate>
      <source>2e5b03a0-fbd5-4215-a650-7d121ffb6635</source>
      <selected>false</selected>
      <category internal="true">dataset</category>
    </geonet:info>
    <revisiondate/>
  </metadata>
  <metadata xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
    <metadatacreationdate/>
    <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
      <id>301282</id>
      <uuid>200910030</uuid>
      <schema>iso19139.mcp-1.4</schema>
      <createDate>2010-12-17T00:09:41</createDate>
      <changeDate>2010-12-17T00:09:41</changeDate>
      <source>2e5b03a0-fbd5-4215-a650-7d121ffb6635</source>
      <selected>false</selected>
      <category internal="true">dataset</category>
    </geonet:info>
    <revisiondate/>
  </metadata>
  <metadata xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
    <metadatacreationdate/>
    <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
      <id>301283</id>
      <uuid>200910040</uuid>
      <schema>iso19139.mcp-1.4</schema>
      <createDate>2010-12-17T00:09:58</createDate>
      <changeDate>2010-12-17T00:09:58</changeDate>
      <source>2e5b03a0-fbd5-4215-a650-7d121ffb6635</source>
      <selected>false</selected>
      <category internal="true">dataset</category>
    </geonet:info>
    <revisiondate/>
  </metadata>
  <metadata xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
    <metadatacreationdate/>
    <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
      <id>301284</id>
      <uuid>200910050</uuid>
      <schema>iso19139.mcp-1.4</schema>
      <createDate>2010-12-17T00:10:18</createDate>
      <changeDate>2010-12-17T00:10:18</changeDate>
      <source>2e5b03a0-fbd5-4215-a650-7d121ffb6635</source>
      <selected>false</selected>
      <category internal="true">dataset</category>
    </geonet:info>
    <revisiondate/>
  </metadata>
  <metadata xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
    <metadatacreationdate/>
    <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
      <id>301285</id>
      <uuid>201011000</uuid>
      <schema>iso19139.mcp-1.4</schema>
      <createDate>2010-12-17T00:10:48</createDate>
      <changeDate>2010-12-17T00:10:48</changeDate>
      <source>2e5b03a0-fbd5-4215-a650-7d121ffb6635</source>
      <selected>false</selected>
      <category internal="true">dataset</category>
    </geonet:info>
    <revisiondate/>
  </metadata>
  <metadata xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
    <metadatacreationdate/>
    <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
      <id>301286</id>
      <uuid>201011002</uuid>
      <schema>iso19139.mcp-1.4</schema>
      <createDate>2010-12-17T00:11:17</createDate>
      <changeDate>2010-12-17T00:11:17</changeDate>
      <source>2e5b03a0-fbd5-4215-a650-7d121ffb6635</source>
      <selected>false</selected>
      <category internal="true">dataset</category>
    </geonet:info>
    <revisiondate/>
  </metadata>
  <metadata xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
    <metadatacreationdate/>
    <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
      <id>301288</id>
      <uuid>201011010</uuid>
      <schema>iso19139.mcp-1.4</schema>
      <createDate>2010-12-17T00:12:02</createDate>
      <changeDate>2010-12-17T00:12:02</changeDate>
      <source>2e5b03a0-fbd5-4215-a650-7d121ffb6635</source>
      <selected>false</selected>
      <category internal="true">dataset</category>
    </geonet:info>
    <revisiondate/>
  </metadata>
  <metadata xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
    <metadatacreationdate/>
    <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
      <id>301289</id>
      <uuid>201011020</uuid>
      <schema>iso19139.mcp-1.4</schema>
      <createDate>2010-12-17T00:12:27</createDate>
      <changeDate>2010-12-17T00:12:27</changeDate>
      <source>2e5b03a0-fbd5-4215-a650-7d121ffb6635</source>
      <selected>false</selected>
      <category internal="true">dataset</category>
    </geonet:info>
    <revisiondate/>
  </metadata>
  <metadata xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
    <metadatacreationdate/>
    <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
      <id>301293</id>
      <uuid>201011021</uuid>
      <schema>iso19139.mcp-1.4</schema>
      <createDate>2010-12-17T00:15:30</createDate>
      <changeDate>2010-12-17T00:15:30</changeDate>
      <source>2e5b03a0-fbd5-4215-a650-7d121ffb6635</source>
      <selected>false</selected>
      <category internal="true">dataset</category>
    </geonet:info>
    <revisiondate/>
  </metadata>
  <metadata xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
    <metadatacreationdate/>
    <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
      <id>301290</id>
      <uuid>201011030</uuid>
      <schema>iso19139.mcp-1.4</schema>
      <createDate>2010-12-17T00:14:07</createDate>
      <changeDate>2010-12-17T00:14:07</changeDate>
      <source>2e5b03a0-fbd5-4215-a650-7d121ffb6635</source>
      <selected>false</selected>
      <category internal="true">dataset</category>
    </geonet:info>
    <revisiondate/>
  </metadata>
  <metadata xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
    <metadatacreationdate/>
    <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
      <id>301291</id>
      <uuid>201011040</uuid>
      <schema>iso19139.mcp-1.4</schema>
      <createDate>2010-12-17T00:14:35</createDate>
      <changeDate>2010-12-17T00:14:35</changeDate>
      <source>2e5b03a0-fbd5-4215-a650-7d121ffb6635</source>
      <selected>false</selected>
      <category internal="true">dataset</category>
    </geonet:info>
    <revisiondate/>
  </metadata>
  <metadata xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
    <metadatacreationdate/>
    <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
      <id>301292</id>
      <uuid>201011050</uuid>
      <schema>iso19139.mcp-1.4</schema>
      <createDate>2010-12-17T00:15:01</createDate>
      <changeDate>2010-12-17T00:15:01</changeDate>
      <source>2e5b03a0-fbd5-4215-a650-7d121ffb6635</source>
      <selected>false</selected>
      <category internal="true">dataset</category>
    </geonet:info>
    <revisiondate/>
  </metadata>
  <metadata xmlns:mcp="http://bluenet3.antcrc.utas.edu.au/mcp">
    <metadatacreationdate/>
    <geonet:info xmlns:geonet="http://www.fao.org/geonetwork">
      <id>6780</id>
      <uuid>7dec18bb-c9f4-4d76-a567-36cae263d5a4</uuid>
      <schema>iso19139.mcp-1.4</schema>
      <createDate>2009-10-19T14:23:52</createDate>
      <changeDate>2010-10-08T13:10:04</changeDate>
      <source>2e5b03a0-fbd5-4215-a650-7d121ffb6635</source>
      <selected>false</selected>
      <category internal="true">dataset</category>
    </geonet:info>
    <revisiondate/>
  </metadata>
</response>
"""
		def service = new GeoNetworkRequestService()
		def uuids = service.collectUuids(xml)
		assertEquals 35, uuids.size()
    }
}

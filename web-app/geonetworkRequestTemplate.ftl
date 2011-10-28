<?xml version="1.0" encoding="UTF-8"?>
<request>
  <fast>false</fast>
  <protocol>OGC:WMS-1.1.1-http-get-map</protocol>
  <#if hitsperpage?has_content>
  <hitsperpage>${hitsperpage}</hitsperpage>
  </#if>
  <#if from?has_content>
  <from>${from}</from>
  </#if>
  <#if eastBL?has_content>
  <eastBL>${eastBL}</eastBL>
  </#if>
  <#if southBL?has_content>
  <southBL>${southBL}</southBL>
  </#if>
  <#if northBL?has_content>
  <northBL>${northBL}</northBL>
  </#if>
  <#if westBL?has_content>
  <westBL>${westBL}</westBL>
  </#if>
  <#if relation?has_content>
  <relation>${relation}</relation>
  </#if>
  <#if any?has_content>
  <any>${any}</any>
  </#if>
  <#if title?has_content>
  <title>${title}</title>
  </#if>
  <#if abstract?has_content>
  <abstract>${abstract}</abstract>
  </#if>
  <#if themekey?has_content>
  <themekey>${themekey}</themekey>
  </#if>
  <#if template?has_content>
  <template>${template}</template>
  </#if>
  <#if extended?has_content>
  <extended>${extended}</extended>
  </#if>
  <#if download?has_content>
  <download>${download}</download>
  </#if>
  <#if digital?has_content>
  <digital>${digital}</digital>
  </#if>
  <#if paper?has_content>
  <paper>${paper}</paper>
  </#if>
  <#if group?has_content>
  <group>${group}</group>
  </#if>
  <#if attrset?has_content>
  <attrset>${attrset}</attrset>
  </#if>
  <#if dateFrom?has_content>
  <dateFrom>${dateFrom?string("yyyy-MM-dd")}</dateFrom>
  </#if>
  <#if dateTo?has_content>
  <dateTo>${dateTo?string("yyyy-MM-dd")}</dateTo>
  </#if>
  <#if extFrom?has_content>
  <extFrom>${extFrom?string("yyyy-MM-dd")}</extFrom>
  </#if>
  <#if extTo?has_content>
  <extTo>${extTo?string("yyyy-MM-dd")}</extTo>
  </#if>
  <#if category?has_content>
  <category>${category}</category>
  </#if>
  <#if sortBy?has_content>
  <sortBy>${sortBy}</sortBy>
  </#if>
</request>
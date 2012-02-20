<?xml version="1.0" encoding="UTF-8"?>
<request>
  <#if fast?has_content>
  <fast>${fast}</fast>
  </#if>
  <#if protocol?has_content>
  <protocol>${protocol}</protocol>
  </#if>
  <#if hitsperpage?has_content>
  <hitsperpage>${hitsperpage}</hitsperpage>
  </#if>
  <#if from?has_content>
  <from>${from}</from>
  </#if>
  <#if to?has_content>
  <to>${to}</to>
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
  <#-- Example with has_next peek <#list themekey as item>${item}<#if item_has_next> or </#if></#list> -->
  <#if themekey?has_content>
  <#if themekey?is_enumerable>
  <#list themekey as item>
  <#if item?has_content>
  <themekey>${item}</themekey>
  </#if>
  </#list>
  <#else>
  <themekey>${themekey}</themekey>
  </#if>
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
  <#if category?is_enumerable>
  <#list category as item>
  <#if item?has_content>
  <category>${item}</category>
  </#if>
  </#list>
  <#else>
  <category>${category}</category>
  </#if>
  </#if>
  <#if orgName?has_content>
  <#if orgName?is_enumerable>
  <#list orgName as item>
  <#if item?has_content>
  <orgName>${item}</orgName>
  </#if>
  </#list>
  <#else>
  <orgName>${orgName}</orgName>
  </#if>
  </#if>
  <#if dataparam?has_content>
  <#if dataparam?is_enumerable>
  <#list dataparam as item>
  <#if item?has_content>
  <dataparam>${item}</dataparam>
  </#if>
  </#list>
  <#else>
  <dataparam>${dataparam}</dataparam>
  </#if>
  </#if>
  <#if longParamName?has_content>
  <#if longParamName?is_enumerable>
  <#list longParamName as item>
  <#if item?has_content>
  <longParamName>${item}</longParamName>
  </#if>
  </#list>
  <#else>
  <longParamName>${longParamName}</longParamName>
  </#if>
  </#if>
  <#if sortBy?has_content>
  <sortBy>${sortBy}</sortBy>
  </#if>
</request>
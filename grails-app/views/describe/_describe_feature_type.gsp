
<%--

 Copyright 2012 IMOS

 The AODN/IMOS Portal is distributed under the terms of the GNU General Public License

--%>
<div id="feature_type_description_container">
<g:form name="geoserver_describe_feature_type_form" method="GET" url="[ controller: 'describe', action:'describe' ]" target="_blank">
<g:hiddenField name="getLink" value="${operation?.getLink}" />
<ul>
  <li class="heading">Feature Type Name</li>
  <li><g:textField name="feature_type_name" value="" /></li>
  <li><g:submitButton name="submit" value="Describe" /></li>
</ul>
</g:form>
</div>

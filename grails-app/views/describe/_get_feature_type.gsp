<div id="get_feature_type_container">
<g:form name="geoserver_get_feature_type_form" method="GET" url="[ controller: 'describe', action:'feature' ]" target="_blank">
<g:hiddenField name="getLink" value="${operation?.getLink}" />
<ul>
  <li class="heading">Feature Type Name</li>
  <li><g:textField name="feature_type_name" value="" /></li>
  <li><g:submitButton name="submit" value="Get" /></li>
</ul>
</g:form>
</div>
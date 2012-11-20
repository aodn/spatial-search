
<%--

 Copyright 2012 IMOS

 The AODN/IMOS Portal is distributed under the terms of the GNU General Public License

--%>
<ul>
  <li class="heading">Provider</li>
  <li>${geoserver.provider}</li>
  <li class="heading">Individual</li>
  <li>${geoserver.individual}</li>
  <li class="heading">Position</li>
  <li>${geoserver.position}</li>
  <li class="heading">Operations</li>
  <li>
    <ul>
      <% geoserver.operations.each { operation -> %>
	  <li class="heading">${operation.name}</li>
	  <li>GET ${operation.getLink}</li>
	  <li>POST ${operation.postLink}</li>
	  <li class="subheading">Parameters</li>
	  <li>
	    <ul>
		  <% operation.parameters.each { name, values -> %>
		  <li>${name}</li>
		  <ul>
		    <% values.each { value -> %>
		    <li>${value}</li>
		    <% } %>
		  </ul>
		  <% } %>
	    </ul>
	  </li>
	    <% if ('DescribeFeatureType' == operation.name) { %>
			<g:render template="describe_feature_type" model="[operation: operation]" />
	    <% } else if ('GetFeature' == operation.name) { %>
		    <g:render template="get_feature_type" model="[operation: operation]" />
		<% } %>
      <% } %>
    </ul>
  </li>
</ul>

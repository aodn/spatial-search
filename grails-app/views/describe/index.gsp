
<%--

 Copyright 2012 IMOS

 The AODN/IMOS Portal is distributed under the terms of the GNU General Public License

--%>
<html>
    <head>
        <title>Geoserver Interrogation Form</title>
        <meta name="layout" content="main" />
        <r:require modules="bootstrap"/>
        <g:javascript library="prototype" />
    </head>
    <body>
        <div id="form">
          <g:formRemote name="geoserver_interrogation_form" method="GET" url="[ controller: 'describe', action:'query' ]" update="capabilities_div" class="form-inline">
            <label>Geoserver</label>
            <g:textField name="geoserver" value="" />
            <g:submitButton name="submit" value="Search" class="btn btn-success btn-mini"/>
          </g:formRemote>
        </div>
        <p>
        <div id="capabilities_div"></div>
    </body>
<script type="text/javascript">

</script>
</html>

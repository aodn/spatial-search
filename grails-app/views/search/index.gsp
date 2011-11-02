<html>
    <head>
        <title>Spatial Search Test Form</title>
        <meta name="layout" content="main" />
        <style type="text/css" media="screen">

        #nav {
            margin-top:20px;
            margin-left:30px;
            width:228px;
            float:left;

        }
        .homePagePanel * {
            margin:0px;
        }
        .homePagePanel .panelBody ul {
            list-style-type:none;
            margin-bottom:10px;
        }
        .homePagePanel .panelBody h1 {
            text-transform:uppercase;
            font-size:1.1em;
            margin-bottom:10px;
        }
        .homePagePanel .panelBody {
            background: url(images/leftnav_midstretch.png) repeat-y top;
            margin:0px;
            padding:15px;
        }
        .homePagePanel .panelBtm {
            background: url(images/leftnav_btm.png) no-repeat top;
            height:20px;
            margin:0px;
        }

        .homePagePanel .panelTop {
            background: url(images/leftnav_top.png) no-repeat top;
            height:11px;
            margin:0px;
        }
        h2 {
            margin-top:15px;
            margin-bottom:15px;
            font-size:1.2em;
        }
        #pageBody {
            margin-left:280px;
            margin-right:20px;
        }
        ul {
        	list-style: none;
        }
        </style>
    </head>
    <body>
        <div id="form">
          <g:form id="search_form" action="search">
          <ul>
          	<li>Any</li>
            <li><g:textField name="any" value="" /></li>
          	<li>Keyword</li>
            <li><g:textField name="keyword" value="Sea Mammal Research Unit" /></li>
            <li>North</li>
            <li><g:textField name="northBL" value="" /></li>
            <li>East</li>
            <li><g:textField name="eastBL" value="" /></li>
            <li>South</li>
            <li><g:textField name="southBL" value="" /></li>
            <li>West</li>
            <li><g:textField name="westBL" value="" /></li>
            <li><g:submitButton name="submit" value="Search" /></li>
          </ul>
          </g:form>
        </div>
    </body>
</html>
# Spatial Search
Applies fine grained spatial searches to GeoNetwork metadata based on linked Geoserver features


# Configuration
Ensure that spatial search is configured to index the desired GeoNetwork instance.  See `geonetwork.search.serverURL` and `geonetwork.index.serverURL` in [Config.groovy](grails-app/conf/Config.groovy)

# Usage
The API consists of the following functions with corresponding URLs.

## Harvest
A harvest consists of a [queue](#queue) followed by an [index](#index) for each of the queued records.

To harvest:

```bash
$ curl http://localhost:8080/spatialsearch/index/harvest
Queueing started at Wed Oct 30 12:12:08 EST 2013<br>943 metadata documents queued finishing at Wed Oct 30 12:12:15 EST 2013<br>Indexing started at Wed Oct 30 12:12:15 EST 2013<br>
```

## Queue
A queue will query the configured GeoNetwork for all of its records containing a WMS layer.

To queue:

```bash
$ curl http://localhost:8080/spatialsearch/index/queue
Queueing started at Wed Oct 30 12:09:05 EST 2013<br>943 metadata documents queued finishing at Wed Oct 30 12:09:14 EST 2013<br>
```

## Index
An index will index each of the queued metadata records.  Indexing is the function whereby geoserver instances are hit via WFS and the responses parsed into geometries.

To index:

```bash
$ curl http://localhost:8080/spatialsearch/index/index
Indexing started at Wed Oct 30 12:13:09 EST 2013<br>
```

# Debugging

## GeoNetwork Responses
To view a single record as it would be returned from GeoNetwork to spatial search for queuing use:

```bash 
$ curl http://catalogue-123.aodn.org.au/geonetwork/srv/eng/q?uuid=c1344e70-480e-0993-e044-00144f7bc0f4&fast=index
```

To view the entire list of records that spatial search queues use:

```bash
$ curl http://catalogue-123.aodn.org.au/geonetwork/srv/eng/q?from=1&to=15&protocol=OGC:WMS-1.1.1-http-get-map%20or%20OGC:WMS-1.3.0-http-get-map&fast=index
```

changing `from` and `to` parameters to paginate.


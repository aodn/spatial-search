package au.org.emii.search


import java.sql.Timestamp
import java.sql.Types;
import java.text.SimpleDateFormat;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate


import au.org.emii.search.index.IndexRun
import au.org.emii.search.index.GeonetworkMetadata
import au.org.emii.search.geometry.GeometryHelper

class GeoNetworkRequestService implements ApplicationContextAware {

	ApplicationContext applicationContext
	
	def grailsApplication
	def geoNetworkRequest
	def executorService
	def searchSummaryService
	def dataSource

	static transactional = true
	
	def queue(params) {
		def metadata = []
		def queueSize = _peek(params)
		if (queueSize == 0) {
			return metadata
		}
		
		// We could have large datasets especially on a forced or new index build
		// so let's paginate
		params.from = '1'
		params.to = '200'
		metadata.addAll(_queuePage(params))
		while (_pageForward(params, queueSize)) {
			metadata.addAll(_queuePage(params))
		}
		
		return metadata
	}
	
	def search(params) {
		if (params.protocol && _isBoundingBoxSubmitted(params)) {
            return _spatialSearch(params)
		}
		return _geoNetworkSearch(params)
	}
	
	def _queuePage(params) {
		// Add the date params so we only fetch metadata records that have been
		// modified since last run
		_addCommonQueueParams(params)
		
		def url = grailsApplication.config.geonetwork.index.serverURL
		def xml = geoNetworkRequest.request(url, params)
		def geoNetworkResponse = new GeoNetworkResponse(grailsApplication, xml)
		def pageMetadata = geoNetworkResponse.getGeonetworkMetadataObjects()
		_saveGeonetworkMetadata(pageMetadata)
		return pageMetadata
	}
	
	def _geoNetworkSearch(params) {
		def url = grailsApplication.config.geonetwork.search.serverURL
		_addFastIndexParams(params)
		log.info("Searching geonetwork instance at $url with $params")
		return geoNetworkRequest.request(url, params)
	}
	
	def _spatialSearch(params) {
		params.relation = 'intersects'
		def numberOfResultsToReturn = _getPageSize(params)
		def pageSize = numberOfResultsToReturn

		if (_isPaging(params)) {
			_updateNumericParam(params, 'to', _getPageEnd(params))
		}
		
		def spatialResponse = new SpatialSearchResponse(grailsApplication, params, numberOfResultsToReturn, executorService, _getGeoNetworkSearchSummaryServiceBean())
		while (_addSpatialResponse(params, spatialResponse)) {}
		return spatialResponse.getResponse()
	}
	
	def _addSpatialResponse(params, spatialResponse) {
		def xml = _geoNetworkSearch(params)
		def geoNetworkResponse = new GeoNetworkResponse(grailsApplication, xml)
		def features = _searchForFeatures(params, geoNetworkResponse.getUuids())
		log.debug("There are ${features?.size()} matching spatial criteria in this response")
		return spatialResponse.addResponse(features, geoNetworkResponse) && _pageForward(params, geoNetworkResponse.count)
	}

	def _searchForFeatures(params, uuids) {
		def featureUuids = []
		if (uuids) {
			def jdbcTemplate = new NamedParameterJdbcTemplate(dataSource)
			try {
				featureUuids = jdbcTemplate.queryForList(
					"select geonetwork_uuid from feature_type where intersects(geometry, ST_GeomFromText(:wkt, :srid)) and geonetwork_uuid in (:uuids)",
					_getSqlParamaterSourceMap(_getGeometry(params).toText(), GeometryHelper.SRID, uuids),
					String.class
				)
			}
			catch (DataAccessException e) {
				log.error('', e)
			}
			catch (Exception e) {
				log.error('', e)
			}
		}
		return featureUuids
	}
	
	def _getSqlParamaterSourceMap(geometryWkt, srid, uuids) {
		return new MapSqlParameterSource()
			.addValue('wkt', geometryWkt)
			.addValue('srid', srid)
			.addValue('uuids', uuids)
	}

	def _saveGeonetworkMetadata(metadataCollection) {
		def metadataRecords = GeonetworkMetadata.findAllByIndexRunIsNull()
		def added = new Timestamp(new Date().time)
		metadataCollection.removeAll(metadataRecords)
		metadataCollection.each { metadata ->
			metadata.added = added
			metadata.save(failOnError: true)
		}
	}
	
	def _getLastRun() {
		def c = IndexRun.createCriteria()
		def lastRun = c.get {
			projections {
				max('runDate')
			}
		}
		if (lastRun == null) {
			lastRun = _initialiseLastRun()
		}
		log.debug(lastRun)
		return lastRun
	}
	
	def _initialiseLastRun() {
		return new Timestamp(new Date(0).time)
	}
	
	def _getGeometry(params) {
		def north = params.northBL
		def east = params.eastBL
		def south = params.southBL
		def west = params.westBL
		
		def helper = new GeometryHelper()
		return helper.toBoundingBox(north, east, south, west)
	}
	
	def _pageForward(params, stopper) {
		assert stopper instanceof Integer, "Paging stopper is not an an Integer"
		
		def moved = false
		def to = _getNumericParam(params, 'to')
		if (to < stopper) {
			// There are more records that we can check against automatically
			// page forward
			_updateNumericParam(params, 'from', to + 1)
			_updateNumericParam(params, 'to', to + 200)
			moved = true
		}
		return moved
	}
	
	def _isBoundingBoxSubmitted(params) {
		return params.northBL && params.eastBL && params.southBL && params.westBL
	}
	
	def _isForced(params) {
		def force = false
		if (params.force) {
			force = new Boolean(params.force)
		}
		return force.booleanValue()
	}
	
	def _isPaging(params) {
		return params.from && params.to
	}
	
	def _getPageSize(params) {
        if (!params.pageSize) {
            def from = _getNumericParam(params, 'from')
            def to = _getNumericParam(params, 'to')
            params.pageSize = _getPageSize(from, to)
        }
		return params.pageSize
	}
	
	def _getPageSize(from, to) {
		def pageSize = 0
		if (from && to) {
			pageSize = to - from + 1
		}
		return pageSize
	}
	
	def _getNumericParam(params, name) {
		try {
			return Integer.valueOf(params[name])
		}
		catch (NumberFormatException nfe) {
			// We can't determine pagination so return an empty geonetwork
			// response but log something in case this keeps on occurring
			log.error("Cannot parse '$name' parameter to integer", nfe)
		}
		return null
	}
	
	def _updateNumericParam(params, name, value) {
		params[name] = String.valueOf(value)
	}
	
	def _peek(params) {
		// Peek ahead to see how many records are going to be queued
		_addCommonQueueParams(params)
		params.from = '1'
		params.to = '1'
		
		def url = grailsApplication.config.geonetwork.index.serverURL
		log.info("Using geonetwork instance at $url indexing changes between $params.dateFrom and $params.dateTo")
		def xml = geoNetworkRequest.request(url, params)
		def geoNetworkResponse = new GeoNetworkResponse(grailsApplication, xml)
		return geoNetworkResponse.count
	}
	
	def _addCommonQueueParams(params) {
		def lastRun = _isForced(params) ? _initialiseLastRun() : _getLastRun()
		params.dateFrom = _dateToString(lastRun)
		params.dateTo = _dateToString(_tomorrow())
		params.protocol = grailsApplication.config.geonetwork.request.protocol
		_addFastIndexParams(params)
	}
	
	def _addFastIndexParams(params) {
		params.fast = 'index'
	}
	
	def _dateToString(date) {
		def sdf = new SimpleDateFormat("yyyy-MM-dd")
		return sdf.format(date)
	}
	
	def _tomorrow() {
		def cal = new GregorianCalendar()
		cal.add(Calendar.DATE, 1)
		return cal.getTime()
	}
	
	def _getPageEnd(params) {
		def pageEnd = grailsApplication.config.geonetwork.search.page.size.toInteger()
		def to = _getNumericParam(params, 'to')
		if (to && to >= pageEnd) {
			return to + pageEnd
		}
		return pageEnd
	}
	
	def synchronized _getGeoNetworkSearchSummaryServiceBean() {
		if (!searchSummaryService) {
			searchSummaryService = applicationContext.getBean('geoNetworkSearchSummaryService')
		}
		return searchSummaryService
	}
}

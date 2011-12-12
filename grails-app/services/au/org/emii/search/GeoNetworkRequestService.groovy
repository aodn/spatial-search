package au.org.emii.search

import groovy.xml.MarkupBuilder;
import groovy.xml.StreamingMarkupBuilder;

import java.sql.Timestamp

import org.hibernate.criterion.Restrictions;
import org.hibernatespatial.criterion.SpatialRestrictions;
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

import com.vividsolutions.jts.io.ParseException;

import au.org.emii.search.index.IndexRun
import au.org.emii.search.index.GeonetworkMetadata
import au.org.emii.search.geometry.GeometryHelper

class GeoNetworkRequestService implements ApplicationContextAware {

	ApplicationContext applicationContext
	
	def grailsApplication
	def geoNetworkRequest
	
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
		params.to = '50'
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
		def lastRun = _isForced(params) ? _initialiseLastRun() : _getLastRun()
		params.dateFrom = lastRun
		params.dateTo = new Date()
		params.fast = 'false'
		params.protocol = grailsApplication.config.geonetwork.request.protocol
		
		def url = grailsApplication.config.geonetwork.index.serverURL
		def xml = geoNetworkRequest.request(url, params)
		def geoNetworkResponse = new GeoNetworkResponse(grailsApplication, xml)
		def pageMetadata = geoNetworkResponse.getGeonetworkMetadataObjects()
		_saveGeonetworkMetadata(pageMetadata)
		return pageMetadata
	}
	
	def _geoNetworkSearch(params) {
		// Ensure we get back all the data we need to present to the user
		params.fast = 'false'
		def url = grailsApplication.config.geonetwork.search.serverURL
		def xml = geoNetworkRequest.request(url, params)
		return xml
	}
	
	def _spatialSearch(params) {
		params.relation = 'intersects'
		
		def pageSize = null
		if (_isPaging(params)) {
			// Grab an additional two pages worth from geonetwork i.e. three pages
			def to = _getNumericParam(params, 'to')
			pageSize = _getPageSize(params)
			def pageEnd = pageSize * 2 + to
			_updateNumericParam(params, 'to', pageEnd)
		}
		
		def xml = _geoNetworkSearch(params)
		def geoNetworkResponse = new GeoNetworkResponse(grailsApplication, xml)
		def metadataCollection = geoNetworkResponse.getGeonetworkMetadataObjects()
		
		def features = _searchForFeatures(params, geoNetworkResponse.uuids)
		xml = geoNetworkResponse.getSpatialResponse(metadataCollection, features, pageSize)
		
		if (!features && _pageForward(params, geoNetworkResponse.count)) {
			xml = _spatialSearch(params)
		}
		return xml
	}

	def _searchForFeatures(params, uuids) {
		def features = []
		if (uuids) {
			try {
				def box = _getGeometry(params)
				def crit = FeatureType.createCriteria()
				features = crit.list {
					add(SpatialRestrictions.intersects('geometry', box))
					add(Restrictions.in('geonetworkUuid', uuids))
				}
			}
			catch (ParseException pe) {
				// TODO add info to the returned XML to indicate no spatial search was performed
			}
		}
		return features
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
			def pageSize = _getPageSize(params)
			_updateNumericParam(params, 'from', to + 1)
			_updateNumericParam(params, 'to', to + pageSize)
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
		def from = _getNumericParam(params, 'from')
		def to = _getNumericParam(params, 'to')
		def pageSize = _getPageSize(from, to)
		return pageSize
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
		def lastRun = _isForced(params) ? _initialiseLastRun() : _getLastRun()
		params.dateFrom = lastRun
		params.dateTo = new Date()
		params.fast = 'true'
		params.protocol = grailsApplication.config.geonetwork.request.protocol
		params.from = '1'
		params.to = '1'
		
		def url = grailsApplication.config.geonetwork.index.serverURL
		def xml = geoNetworkRequest.request(url, params)
		def geoNetworkResponse = new GeoNetworkResponse(grailsApplication, xml)
		return geoNetworkResponse.count
	}
}

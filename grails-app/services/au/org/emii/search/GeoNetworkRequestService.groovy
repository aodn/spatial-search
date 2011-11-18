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
		def metadataCollection = geoNetworkResponse.getGeonetworkMetadataObjects()
		_saveGeonetworkMetadata(metadataCollection)
		return metadataCollection
	}
	
	def search(params) {
		// Guard code, if there is no protocol specified by the sender (portal)
		// in this case then they're not doing a spatial search and we just
		// return geonetwork results
		if (!(params.protocol && _isBoundingBoxSubmitted(params))) {
			return _geoNetworkSearch(params)
		}
		return _spatialSearch(params)
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
		
		def xml = _geoNetworkSearch(params)
		def geoNetworkResponse = new GeoNetworkResponse(grailsApplication, xml)
		def metadataCollection = geoNetworkResponse.getGeonetworkMetadataObjects()
		
		def features = _searchForFeatures(params, geoNetworkResponse.uuids)
		xml = geoNetworkResponse.getSpatialResponse(metadataCollection, features)
		
		if (!features && _pageForward(params, geoNetworkResponse)) {
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
	
	def _pageForward(params, geoNetworkResponse) {
		def moved = false
		try {
			def from = Integer.valueOf(params.from)
			def to = Integer.valueOf(params.to)
			if (to < geoNetworkResponse.count) {
				// There are more records that we can check against automatically
				// page forward
				def pageSize = to - from + 1
				params.from = String.valueOf((from + pageSize))
				params.to = String.valueOf((to + pageSize))
				moved = true
			}
		}
		catch (NumberFormatException nfe) {
			// We can't determine pagination so return an empty geonetwork
			// response but log something in case this keeps on occurring
			log.error("Cannot parse 'from' or 'to' parameter for pagination", nfe)
		}
		return moved
	}
	
	def _isBoundingBoxSubmitted(params) {
		return params.northBL && params.eastBL && params.southBL && params.westBL
	}
	
	def _isForced(params) {
		return params.force && params.force.booleanValue()
	}
}

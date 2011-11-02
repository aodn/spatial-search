package au.org.emii.search

import java.sql.Timestamp

import org.hibernate.criterion.Restrictions;
import org.hibernatespatial.criterion.SpatialRestrictions;
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

import au.org.emii.search.index.IndexRun
import au.org.emii.search.index.QueuedDocument
import au.org.emii.search.geometry.GeometryHelper

class GeoNetworkRequestService implements ApplicationContextAware {

	ApplicationContext applicationContext
	
	def grailsApplication
	def geoNetworkRequest
	
	static transactional = true
	
	def queue(params) {
		// Add the date params so we only fetch metadata records that have been
		// modified since last run
		def lastRun = getLastRun()
		params['dateFrom'] = lastRun
		params['dateTo'] = new Date()
		params['fast'] = 'false'
		params['protocol'] = grailsApplication.config.geonetwork.protocol
		
		def url = grailsApplication.config.geonetwork.serverURL
		def xml = geoNetworkRequest.request(url, params)
		
		def documents = parseXmlToQueuedDocuments(xml)
		saveQueuedDocuments(documents)
		return documents
	}
	
	def search(params) {
		// Ensure we get shorthand results	
		params['fast'] = 'true'
		params['protocol'] = grailsApplication.config.geonetwork.protocol
		params['relation'] = 'intersects'
		
		def url = grailsApplication.config.geonetwork.serverURL
		def xml = geoNetworkRequest.request(url, params)
		def uuids = collectUuids(xml)
		
		def features
		if (uuids) {
			def north = params['northBL']
			def east = params['eastBL']
			def south = params['southBL']
			def west = params['westBL']
			
			def helper = new GeometryHelper()
			def box = helper.toGeometry('Polygon', "${north} ${west} ${north} ${east} ${south} ${east} ${south} ${west} ${north} ${west}")
			
			def crit = FeatureType.createCriteria()
			features = crit.list {
				add(SpatialRestrictions.intersects('geometry', box))
				add(Restrictions.in('geonetworkUuid', uuids))
			}
		}
		return features ? features : []
	}

	def saveQueuedDocuments(documents) {
		def queuedDocuments = QueuedDocument.findAllByIndexRunIsNull()
		def added = new Timestamp(new Date().time)
		documents.removeAll(queuedDocuments)
		documents.each { document ->
			document.added = added
			document.save(failOnError: true)
		}
	}
	
	def getLastRun() {
		def c = IndexRun.createCriteria()
		def lastRun = c.get {
			projections {
				max('runDate')
			}
		}
		if (lastRun == null) {
			lastRun = new Timestamp(new Date(0).time)
		}
		log.debug(lastRun)
		return lastRun
	}
	
	def parseXmlToQueuedDocuments(xml) {
		def tree = new XmlSlurper().parseText(xml)
		def allRecords = tree.metadata
		log.debug("${allRecords.size()} metadata records returned")
		
		return parseMetadataElements(tree)
	}
	
	def parseMetadataElements(tree) {
		def documents = [] as Set
		tree.metadata.each { metadata ->
			def uuid = metadata.info.uuid.text()
			metadata.link.each { link ->
				def document = parseLinkElement(link)
				if (document) {
					document.geonetworkUuid = uuid
					documents << document
				}
			}
		}
		return documents
	}
	
	def parseLinkElement(link) {
		def document
		def protocol = grailsApplication.config.geonetwork.protocol
		def regexPattern = grailsApplication.config.geonetwork.feature.type.indentifier.regex
		if (link.@protocol.text() =~ protocol && link.@name.text() =~ /$regexPattern/) {
			document = new QueuedDocument()
			document.featureTypeName = link.@name.text()
			document.geoserverEndPoint = serverEndPointFrom(link.@href.text())
		}
		return document
	}
	
	def serverEndPointFrom(url) {
		return url.substring(0, url.lastIndexOf("/"))
	}
	
	def collectUuids(xml) {
		def tree = new XmlSlurper().parseText(xml)
		return tree.metadata.info.uuid.collect { uuid -> uuid.text() }
	}
}


/*
 * Copyright 2012 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */
package au.org.emii.search.index

import org.xml.sax.SAXParseException

import javax.xml.parsers.SAXParserFactory

import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.apache.http.HttpResponse
import org.apache.http.client.ResponseHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.xml.sax.InputSource

class DiskCachingFeatureTypeRequest extends FeatureTypeRequest implements ResponseHandler<String> {

    static Logger log = LoggerFactory.getLogger(DiskCachingFeatureTypeRequest.class)

    /*
     * This class is used/extended by features that return large XML results
     * the XML is written to disk first to complete the request as quickly as
     * possible then it is parsed afterward
     */

    def featureCallback
    def metadataCallback

    DiskCachingFeatureTypeRequest() {
        super()
    }

    DiskCachingFeatureTypeRequest(String featureTypeIdElementName, String featureTypeGeometryElementName) {
        super(featureTypeIdElementName, featureTypeGeometryElementName)
    }

    DiskCachingFeatureTypeRequest(String featureTypeIdElementName) {
        super(featureTypeIdElementName)
    }

    DiskCachingFeatureTypeRequest(String featureTypeIdElementName, String featureTypeGeometryElementName, String namespaceAware) {
        super(featureTypeIdElementName, featureTypeGeometryElementName, namespaceAware)
    }

    def handleResponse(metadata, filePath) {
        def features = [] as Set
        if (filePath) {
            def inputReader
            def delete = false
            try {
                def handler = new DiskCachingFeatureTypeParser(metadata, this)
                def reader = SAXParserFactory.newInstance().newSAXParser().XMLReader
                reader.setContentHandler(handler)
                inputReader = new BufferedReader(new FileReader(filePath))
                reader.parse(new InputSource(inputReader))
                delete = true
            }
            catch (SAXParseException spe) {
                log.error("Could not parse cached xml response from ${filePath}", spe)
            }
            catch (IOException ioe) {
                log.error("Could not read cached xml response from ${filePath}", ioe)
            }
            finally {
                IOUtils.closeQuietly(inputReader)
                if (delete) {
                    FileUtils.deleteQuietly(new File(filePath))
                }
            }
        }
        return features
    }

    def getResponseHandler() {
        return this
    }

    def _cache(inStream) {
        def cached = ""
        def cacheDir = grailsApplication.config.geoserver.response.cache.dir
        log.debug("Using cache dir ${cacheDir}")

        def writer
        def file
        try {
            file = new File("${cacheDir}${File.separator}${featureTypeElementName}.xml")
            writer = new BufferedWriter(new FileWriter(file))
            IOUtils.copy(inStream, writer)
            cached = file.getAbsolutePath()
        }
        catch (IOException ioe) {
            log.error("Error caching ${file.name}: ", ioe)
        }
        finally {
            IOUtils.closeQuietly(writer)
        }
        return cached
    }

    String handleResponse(HttpResponse response) {
        return _cache(response.getEntity().getContent())
    }
}

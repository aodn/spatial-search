
/*
 * Copyright 2012 IMOS
 *
 * The AODN/IMOS Portal is distributed under the terms of the GNU General Public License
 *
 */
package au.org.emii.search.index

class FeatureTypeRequestClass {

    String featureTypeName
    String className
    String constructorArgs
    String featureMembersElementName
    String outputFormat

    def featureTypeRequest
    def grailsApplication

    static constraints = {
        constructorArgs(nullable: true)
    }

    def getFeatureTypeRequest() {
        def clazz = grailsApplication.getClassLoader().loadClass(className)

        def args
        if (constructorArgs) {
            args = constructorArgs.split(',')
        }

        if (!args) {
            log.debug("Instantiating class ${className} with zero arg constructor")
            featureTypeRequest = clazz.newInstance()
            featureTypeRequest.grailsApplication = grailsApplication
        }
        else {
            def argsClasses = new Class[args.length]
            for (def i = 0; i < args.length; i++) {
                argsClasses[i] = String.class
            }
            log.debug("Instantiating class ${className} with constructor args ${args}")
            def constructor = clazz.getConstructor(argsClasses)
            featureTypeRequest = constructor.newInstance(args)
            featureTypeRequest.grailsApplication = grailsApplication
            featureTypeRequest.configure(this)
        }
        return featureTypeRequest
    }
}

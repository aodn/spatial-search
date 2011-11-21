package au.org.emii.search.index


class HarvesterJob {

	def geoNetworkRequestService
	def featureTypeRequestService
	
	static triggers = {
		cron name: 'harvester', cronExpression: "0 0 3 * * ?"
	}
	
    def execute() {
        geoNetworkRequestService.queue([:])
		featureTypeRequestService.index()
    }
}

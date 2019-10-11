package com.example.chargestation.controller.request

import spock.lang.Specification

/**
 *	Unit tests for {@link StartSessionRequest}
 */
class StartSessionRequestTest extends Specification {

	def stationId = 'station_id'

	def chargeSessionRequest = new StartSessionRequest(stationId: stationId)

	def "sut returns initialized station id"() {
		expect:
			chargeSessionRequest.getStationId() == stationId
	}

	def "sut updates station is wth provided value"() {
		given:
			def newStationId = 'new_station_id'

		when:
			chargeSessionRequest.setStationId(newStationId)

		then:
			chargeSessionRequest.stationId == newStationId
	}
}

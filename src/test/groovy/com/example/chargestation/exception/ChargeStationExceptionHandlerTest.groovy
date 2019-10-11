package com.example.chargestation.exception

import org.springframework.http.HttpStatus
import spock.lang.Shared
import spock.lang.Specification

/**
 *	Unit test for {@link ChargeStationExceptionHandler}
 */
class ChargeStationExceptionHandlerTest extends Specification {

	@Shared
	def chargeStationExceptionHandler = new ChargeStationExceptionHandler()

	def "SessionNotFoundException is processed correctly"() {
		given:
			def sessionId = 'session_id'
			def exception = new SessionNotFoundException(sessionId)
			def exceptionMessage = 'Charge session with id [session_id] is not found'

		when:
			def actualResult = chargeStationExceptionHandler.handleSessionNotFoundException(exception)

		then:
			with(actualResult) {
				statusCode == HttpStatus.BAD_REQUEST
				body.error == exceptionMessage
			}
	}
}

package com.example.chargestation.exception

import spock.lang.Specification

/**
 *	Unit tests for {@link RestErrorMessage}
 */
class RestErrorMessageTest extends Specification {

	def error = 'error_message'

	def restErrorMessage = new RestErrorMessage(error)

	def "sut returns initialized error value"() {
		expect:
			restErrorMessage.getError() == error
	}

	def "sut updates error is wth provided value"() {
		given:
			def newError = 'new_error'

		when:
			restErrorMessage.setError(newError)

		then:
			restErrorMessage.error == newError
	}

}

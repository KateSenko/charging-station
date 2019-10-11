package com.example.chargestation.controller

import com.example.chargestation.ChargeStationApplication
import com.example.chargestation.controller.request.StartSessionRequest
import com.example.chargestation.entity.StatusEnum
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.hamcrest.core.IsNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.web.context.WebApplicationContext
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

import static org.hamcrest.Matchers.is
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup

/**
 * 	Integration tests for {@link ChargingSessionController}
 */
@SpringBootTest(classes = [ChargeStationApplication.class], webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Stepwise
class ChargingSessionControllerTest extends Specification {

	@Autowired
	WebApplicationContext wac
	MockMvc mvc

	def jsonSlurper = new JsonSlurper()

	@Shared
	def createdSessionId

	def setup() {
		mvc = webAppContextSetup(wac).build()
	}

	def "sut creates and returns session on start request"() {
		given:
			def stationId = 'station_id'
			def startSessionRequest = new StartSessionRequest(stationId: stationId)
			def requestBody = JsonOutput.toJson(startSessionRequest)

		when:
			def startSessionResult = mvc.perform(post('/chargingSessions')
					.contentType('application/json')
					.accept('application/json')
					.content(requestBody))
					.andDo(MockMvcResultHandlers.print())
			extractSessionId(startSessionResult)

		then:
			startSessionResult.andExpect(status().isOk())
					.andExpect(jsonPath('$.id', is(IsNull.notNullValue())))
					.andExpect(jsonPath('$.stationId', is(stationId)))
					.andExpect(jsonPath('$.startedAt', is(IsNull.notNullValue())))
					.andExpect(jsonPath('$.stoppedAt', is(IsNull.nullValue())))
					.andExpect(jsonPath('$.status', is(StatusEnum.IN_PROGRESS as String)))
	}

	def "sut finish charge session on stop request"() {
		given:
			def stationId = 'station_id'

		when:
			def stopSessionResult = mvc.perform(put("/chargingSessions/$createdSessionId")
					.contentType('application/json')
					.accept('application/json'))
					.andDo(MockMvcResultHandlers.print())

		then:
			stopSessionResult.andExpect(status().isOk())
					.andExpect(jsonPath('$.id', is(createdSessionId)))
					.andExpect(jsonPath('$.stationId', is(stationId)))
					.andExpect(jsonPath('$.startedAt', is(IsNull.notNullValue())))
					.andExpect(jsonPath('$.stoppedAt', is(IsNull.notNullValue())))
					.andExpect(jsonPath('$.status', is(StatusEnum.FINISHED as String)))
	}

	def "sut returns all charging sessions"() {
		given:
			def stationId = 'station_id'

		when:
			def getAllSessionsResult = mvc.perform(get("/chargingSessions")
					.contentType('application/json')
					.accept('application/json'))
					.andDo(MockMvcResultHandlers.print())

		then:
			getAllSessionsResult.andExpect(status().isOk())
					.andExpect(jsonPath('$[0].id', is(createdSessionId)))
					.andExpect(jsonPath('$[0].stationId', is(stationId)))
					.andExpect(jsonPath('$[0].startedAt', is(IsNull.notNullValue())))
					.andExpect(jsonPath('$[0].stoppedAt', is(IsNull.notNullValue())))
					.andExpect(jsonPath('$[0].status', is(StatusEnum.FINISHED as String)))
	}

	def "sut returns summary of all charging sessions"() {
		given:
			def expectedTotalCount = 1
			def expectedStartedCount = 0
			def expectedStoppedCount = 1

		when:
			def summaryResult = mvc.perform(get("/chargingSessions/summary"))
					.andDo(MockMvcResultHandlers.print())

		then:
			summaryResult.andExpect(status().isOk())
					.andExpect(jsonPath('$.totalCount', is(expectedTotalCount)))
					.andExpect(jsonPath('$.startedCount', is(expectedStartedCount)))
					.andExpect(jsonPath('$.stoppedCount', is(expectedStoppedCount)))
	}

	def extractSessionId(ResultActions startSessionResult) {
		def content = startSessionResult.andReturn().getResponse().getContentAsString()
		createdSessionId = jsonSlurper.parseText(content).id
	}

}

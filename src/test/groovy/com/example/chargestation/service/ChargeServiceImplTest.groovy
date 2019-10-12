package com.example.chargestation.service

import com.example.chargestation.entity.ChargeSession
import com.example.chargestation.entity.StatusEnum
import com.example.chargestation.exception.SessionNotFoundException
import com.example.chargestation.repository.SessionRepository
import spock.lang.Specification

import java.time.LocalDateTime

/**
 * 	Unit tests for {@link ChargeServiceImpl}
 */
class ChargeServiceImplTest extends Specification {

	def sessionRepository = Mock(SessionRepository)
	def chargeServiceImpl = new ChargeServiceImpl(sessionRepository)

	def defaultStationId = 'station _id'
	def defaultSessionId = 'some_id'
	def defaultStartTime = LocalDateTime.now()

	def 'sut saves charge session with IN_PROGRESS status on start request'() {
		given:
			def defaultStationId = 'station _id'

		when:
			def result = chargeServiceImpl.startCharging(defaultStationId)

		then:
			1 * sessionRepository.save(_)

		and:
			with(result) {
				id != null
				stationId == defaultStationId
				startedAt != null
				stoppedAt == null
				status == StatusEnum.IN_PROGRESS
			}
	}

	def 'sut updates charge session with FINISHED status on stop request'() {
		given:
			def chargeSession = initChargeSession(defaultStationId)

		when:
			def result = chargeServiceImpl.stopCharging(defaultStationId)

		then:
			1 * sessionRepository.findById(defaultStationId) >> chargeSession
		and:
			with(result) {
				id != null
				stationId == defaultStationId
				startedAt == defaultStartTime
				stoppedAt != null
				status == StatusEnum.FINISHED
			}
		and:
			1 * sessionRepository.save(chargeSession)
	}

	def 'sut throws exception on stop request for nonexistent station'() {
		given:
			def nonexistentStationId = 'nonexistent_station_id'
			def errorMessage = 'Charge session with id [nonexistent_station_id] is not found'

		when:
			chargeServiceImpl.stopCharging(nonexistentStationId)

		then:
			1 * sessionRepository.findById(nonexistentStationId)
		and:
			def exception = thrown(SessionNotFoundException)
			exception.message == errorMessage
	}

	def 'sut does not stop already finished charge session'() {
		given:
			def sessionId = 'some_id'
			def finishedChargeSession = new ChargeSession(id: sessionId,
					stationId: defaultStationId,
					status: StatusEnum.FINISHED)

		when:
			chargeServiceImpl.stopCharging(sessionId)

		then:
			1 * sessionRepository.findById(sessionId) >> finishedChargeSession
		and:
			0 * sessionRepository.save(_)
	}

	def 'sut returns all charge sessions on retrieve request'() {
		given:
			def sessionId1 = 'session_id_1'
			def sessionId2 = 'session_id_2'
			def chargeSessions = [initChargeSession(sessionId1),
								  initChargeSession(sessionId2)]

		when:
			def result = chargeServiceImpl.retrieveSessions()

		then:
			1 * sessionRepository.findAll() >> chargeSessions
		and:
			result == chargeSessions
	}

	def 'sut returns sessions summary for the last minute'() {
		given:
			def expectedStartedCount = 1
			def expectedStoppedCount = 2
			def expectedTotalCount = 3
			def stationId1 = 'station_1'
			def stationId2 = 'station_2'
			def stationId3 = 'station_3'
		and:
			def session1 = chargeServiceImpl.startCharging(stationId1)
			def session2 = chargeServiceImpl.startCharging(stationId2)
			def session3 = chargeServiceImpl.startCharging(stationId3)
		and:
			sessionRepository.findById(stationId1) >> session1
			sessionRepository.findById(stationId2) >> session2
			sessionRepository.findById(stationId3) >> session3

		when:
			chargeServiceImpl.stopCharging(stationId1)
			chargeServiceImpl.stopCharging(stationId2)

		and:
			def result = chargeServiceImpl.retrieveSummary()

		then:
			result.startedCount == expectedStartedCount
			result.stoppedCount == expectedStoppedCount
			result.totalCount == expectedTotalCount

	}

	def initChargeSession(String stationId) {
		new ChargeSession(id: defaultSessionId,
				stationId: stationId,
				startedAt: defaultStartTime,
				status: StatusEnum.IN_PROGRESS,
				updateNanoTime: 1L)
	}
}

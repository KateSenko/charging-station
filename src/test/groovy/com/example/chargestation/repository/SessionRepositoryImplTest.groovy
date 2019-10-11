package com.example.chargestation.repository

import com.example.chargestation.entity.ChargeSession
import spock.lang.Specification

/**
 *	Unit tests for {@link SessionRepositoryImpl}
 */
class SessionRepositoryImplTest extends Specification {

	def sessionRepositoryImpl = new SessionRepositoryImpl()

	def "sut saves charge session in memory data structure"() {
		given:
			def sessionId = 'some_id'
			def chargeSession = new ChargeSession(id: sessionId)

		when:
			sessionRepositoryImpl.save(chargeSession)

		then:
			sessionRepositoryImpl.sessions.get(sessionId) == chargeSession
	}

	def "sut returns all saved in memory charge sessions"() {
		given:
			def expectedSessionsCount = 2
			def sessionId1 = 'some_id_1'
			def sessionId2 = 'some_id_2'
			def chargeSession1 = new ChargeSession(id: sessionId1)
			def chargeSession2 = new ChargeSession(id: sessionId2)
			sessionRepositoryImpl.sessions.put(sessionId1, chargeSession1)
			sessionRepositoryImpl.sessions.put(sessionId2, chargeSession2)

		when:
			def result = sessionRepositoryImpl.findAll()

		then:
			result.size() == expectedSessionsCount
			result.contains(chargeSession1)
			result.contains(chargeSession2)
	}

	def "sut return saved in memory charge session by id"() {
		given:
			def sessionId = 'some_id'
			def chargeSession = new ChargeSession(id: sessionId)
			sessionRepositoryImpl.sessions.put(sessionId, chargeSession)

		when:
			def result = sessionRepositoryImpl.findById(sessionId)

		then:
			result == chargeSession
	}
}

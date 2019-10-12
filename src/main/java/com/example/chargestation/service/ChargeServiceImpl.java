package com.example.chargestation.service;

import com.example.chargestation.entity.ChargeSession;
import com.example.chargestation.entity.StatusEnum;
import com.example.chargestation.controller.response.SummaryResponse;
import com.example.chargestation.exception.SessionNotFoundException;
import com.example.chargestation.repository.SessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.locks.ReentrantLock;

public class ChargeServiceImpl implements ChargeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChargeServiceImpl.class);
    private static final String UNEXPECTED_ERROR_MESSAGE = "Unexpected exception";

    private static final long SUMMARY_TIME_INTERVAL_IN_NANOSEC = 60_000_000_000L;

    private ConcurrentSkipListMap<Long, String> startTimeSessionIds = new ConcurrentSkipListMap<>();
    private ConcurrentSkipListMap<Long, String> stopTimeSessionIds = new ConcurrentSkipListMap<>();

    private ReentrantLock startSessionLock = new ReentrantLock();
    private ReentrantLock stopSessionLock = new ReentrantLock();
    private ReentrantLock summaryLock = new ReentrantLock();

    private final SessionRepository sessionRepository;

    public ChargeServiceImpl(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    /**
     * Start charge session.
     *
     * Required complexity: O(log (n))
     * Actual complexity: O(log (n))
     *
     * @param stationId the station id
     * @return the charge session
     */
    @Override
    public ChargeSession startCharging(String stationId) {
        Long currentNanoTime = System.nanoTime();
        String sessionId = UUID.randomUUID().toString();

        ChargeSession chargeSession = new ChargeSession();
        chargeSession.setId(sessionId);
        chargeSession.setStationId(stationId);
        chargeSession.setStatus(StatusEnum.IN_PROGRESS);
        chargeSession.setStartedAt(LocalDateTime.now());
        chargeSession.setUpdateNanoTime(currentNanoTime);

        startSessionLock.lock();
        try {
            sessionRepository.save(chargeSession);                  // O(1)
            startTimeSessionIds.put(currentNanoTime, sessionId);    // O(log (n))
        } catch (Exception e) {
            startSessionLock.unlock();
            LOGGER.error(UNEXPECTED_ERROR_MESSAGE, e);
        }
        return chargeSession;
    }

    /**
     * Stop charge session.
     *
     * Required complexity: O(log (n))
     * Actual complexity: O(log (n))
     *
     * @param sessionId the session id
     * @return the charge session
     */
    @Override
    public ChargeSession stopCharging(String sessionId) throws SessionNotFoundException {
        ChargeSession chargeSession = sessionRepository.findById(sessionId);  // O(1)

        if (chargeSession == null) {
            throw new SessionNotFoundException(sessionId);
        }

        startTimeSessionIds.remove(chargeSession.getUpdateNanoTime());         //O(log (n))

        Long currentNanoTime = System.nanoTime();
        chargeSession.setStatus(StatusEnum.FINISHED);
        chargeSession.setStoppedAt(LocalDateTime.now());
        chargeSession.setUpdateNanoTime(currentNanoTime);

        stopSessionLock.lock();
        try {
            stopTimeSessionIds.put(currentNanoTime, sessionId);                     // O(log (n))
            sessionRepository.save(chargeSession);                                  // O(1)
        } catch (Exception e) {
            stopSessionLock.unlock();
            LOGGER.error(UNEXPECTED_ERROR_MESSAGE, e);
        }

        return chargeSession;
    }

    /**
     * Retrieve sessions list.
     *
     * Required complexity: O(n)
     * Actual complexity: O(1)
     *
     * @return the list of charge sessions
     */
    @Override
    public List<ChargeSession> retrieveSessions() {
        return sessionRepository.findAll();  // O(1)
    }

    /**
     * Retrieve sessions response.
     *
     * Required complexity: O(log (n))
     * Actual complexity: O(1)
     *
     * @return the summary response
     */
    @Override
    public SummaryResponse retrieveSummary() {

        Long minuteAgoTime = System.nanoTime() - SUMMARY_TIME_INTERVAL_IN_NANOSEC;
        Integer startedCount = 0;
        Integer stoppedCount = 0;

        summaryLock.lock();
        try {
            startedCount = startTimeSessionIds.tailMap(minuteAgoTime).size();   // O(1)
            stoppedCount = stopTimeSessionIds.tailMap(minuteAgoTime).size();    // O(1)
        } catch (Exception e) {
            summaryLock.unlock();
            LOGGER.error(UNEXPECTED_ERROR_MESSAGE, e);
        }

        SummaryResponse summaryResponse = new SummaryResponse();
        summaryResponse.setStartedCount(startedCount);
        summaryResponse.setStoppedCount(stoppedCount);
        summaryResponse.setTotalCount(startedCount  + stoppedCount);

        return summaryResponse;
    }
}

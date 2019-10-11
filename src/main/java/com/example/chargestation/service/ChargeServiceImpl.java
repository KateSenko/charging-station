package com.example.chargestation.service;

import com.example.chargestation.entity.ChargeSession;
import com.example.chargestation.entity.StatusEnum;
import com.example.chargestation.controller.response.SummaryResponse;
import com.example.chargestation.exception.SessionNotFoundException;
import com.example.chargestation.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * The type Charge service.
 */
public class ChargeServiceImpl implements ChargeService {

    private static final long SUMMARY_TIME_INTERVAL_IN_NANOSEC = 60_000_000_000L;

    private ConcurrentSkipListMap<Long, String> startTimeSessionIds = new ConcurrentSkipListMap<>();
    private ConcurrentSkipListMap<Long, String> stopTimeSessionIds = new ConcurrentSkipListMap<>();

    @Autowired
    private SessionRepository sessionRepository;

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
        Long currentTime = System.currentTimeMillis();
        String sessionId = UUID.randomUUID().toString();

        ChargeSession chargeSession = new ChargeSession();
        chargeSession.setId(sessionId);
        chargeSession.setStationId(stationId);
        chargeSession.setStatus(StatusEnum.IN_PROGRESS);
        chargeSession.setStartedAt(currentTime);

        sessionRepository.save(chargeSession);              // O(1)

        startTimeSessionIds.put(currentTime, sessionId);    // O(log (n))

        return chargeSession;
    }

    /**
     * Stop charge session.
     *
     * Required complexity: O(log n)
     * Actual complexity: O(log (n))
     *
     * @param stationId the station id
     * @return the charge session
     */
    @Override
    public ChargeSession stopCharging(String stationId) throws SessionNotFoundException {
        ChargeSession chargeSession = sessionRepository.findById(stationId);      // O(1)
        if (chargeSession == null) {
            throw new SessionNotFoundException(stationId);
        }

        Long currentTime = System.currentTimeMillis();

        chargeSession.setStatus(StatusEnum.FINISHED);
        chargeSession.setStoppedAt(currentTime);

        sessionRepository.save(chargeSession);                      // O(1)

        stopTimeSessionIds.put(currentTime, stationId);             // O(log (n))
        startTimeSessionIds.remove(chargeSession.getStartedAt());   //?

        return chargeSession;
    }

    /**
     * Retrieve sessions list.
     *
     * Required complexity: O(n)
     * Actual complexity: O(1)
     *
     * @return the list
     */
    @Override
    public List<ChargeSession> retrieveSessions() {
        return sessionRepository.findAll();  // O(1)
    }

    /**
     * Retrieve sessions response.
     *
     * Required complexity: O(log n)
     * Actual complexity: O(1)
     *
     * @return the summary response
     */
    @Override
    public SummaryResponse retrieveSummary() {
        Long minuteAgoTime = System.currentTimeMillis() - 60000;
        Integer startedCount = startTimeSessionIds.tailMap(minuteAgoTime).size();   // O(1)
        Integer stoppedCount = stopTimeSessionIds.tailMap(minuteAgoTime).size();    // O(1)

        SummaryResponse summaryResponse = new SummaryResponse();
        summaryResponse.setStartedCount(startedCount);
        summaryResponse.setStoppedCount(stoppedCount);
        summaryResponse.setTotalCount(startedCount  + stoppedCount);

        return summaryResponse;
    }
}

package com.example.chargestation.service;

import com.example.chargestation.entity.ChargeSession;
import com.example.chargestation.entity.StatusEnum;
import com.example.chargestation.entity.SummaryResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

/**
 * The type Charge service.
 */
public class ChargeServiceImpl implements ChargeService {

    private static final long SUMMARY_TIME_INTERVAL_IN_MIN = 1L;

    private TreeMap<LocalDateTime, String> startTimeSessionIds = new TreeMap<>();
    private TreeMap<LocalDateTime, String> stopTimeSessionIds = new TreeMap<>();
    private TreeMap<LocalDateTime, String> totalTimeSessionIds = new TreeMap<>();
    private Map<String, ChargeSession> sessions = new HashMap<>();

    /**
     * Start session charge session.
     *
     * Required complexity: O(log (n))
     * Actual complexity: O(log (n))
     *
     * @param stationId the station id
     * @return the charge session
     */
    @Override
    public ChargeSession startSession(String stationId) {
        LocalDateTime currentTime = LocalDateTime.now();
        String sessionId = UUID.randomUUID().toString();

        ChargeSession chargeSession = new ChargeSession();
        chargeSession.setId(sessionId);
        chargeSession.setStationId(stationId);
        chargeSession.setStatus(StatusEnum.IN_PROGRESS);
        chargeSession.setStartedAt(currentTime);

        sessions.put(sessionId, chargeSession);             // O(1)

        startTimeSessionIds.put(currentTime, sessionId);    // O(log (n))
        totalTimeSessionIds.put(currentTime, sessionId);    // O(log (n))

        return chargeSession;
    }

    /**
     * Stop charging charge session.
     *
     * Required complexity: O(log n)
     * Actual complexity: O(log (n))
     *
     * @param stationId the station id
     * @return the charge session
     */
    @Override
    public ChargeSession stopCharging(String stationId) {
        LocalDateTime currentTime = LocalDateTime.now();

        ChargeSession chargeSession = sessions.get(stationId);      // O(1)
        chargeSession.setStatus(StatusEnum.FINISHED);
        chargeSession.setStoppedAt(currentTime);

        sessions.put(stationId, chargeSession);                     // O(1)

        stopTimeSessionIds.put(currentTime, stationId);
        totalTimeSessionIds.remove(chargeSession.getStartedAt());   // O(log (n))
        totalTimeSessionIds.put(currentTime, stationId);            // O(log (n))

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
        return new ArrayList<>(sessions.values());  // O(1)
    }

    /**
     * Retrieve summary response.
     *
     * Required complexity: O(log n)
     * Actual complexity: O(1)
     *
     * @return the summary response
     */
    @Override
    public SummaryResponse retrieveSummary() {
        LocalDateTime minuteAgoTime = LocalDateTime.now().minusMinutes(SUMMARY_TIME_INTERVAL_IN_MIN);

        SummaryResponse summaryResponse = new SummaryResponse();
        summaryResponse.setTotalCount(totalTimeSessionIds.tailMap(minuteAgoTime).size());   // O(1)
        summaryResponse.setStartedCount(startTimeSessionIds.tailMap(minuteAgoTime).size()); // O(1)
        summaryResponse.setStoppedCount(stopTimeSessionIds.tailMap(minuteAgoTime).size());  // O(1)

        return summaryResponse;
    }
}

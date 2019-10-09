package com.example.chargestation.service;

import com.example.chargestation.entity.SummaryResponse;
import com.example.chargestation.entity.ChargeSession;
import com.example.chargestation.entity.StatusEnum;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ChargeService {

    private static final int ONE_MINUTE = 60*1000;

    private TreeMap<LocalDateTime, String> startTimeSessionIds = new TreeMap<>();
    private TreeMap<LocalDateTime, String>  stopTimeSessionIds = new TreeMap<>();
    private TreeMap<LocalDateTime, String>  totalTimeSessionIds = new TreeMap<>();
    private Map<String, ChargeSession> sessions = new HashMap<>();

    public ChargeSession startSession(String stationId) {
        LocalDateTime currentTime = LocalDateTime.now();
        String sessionId = UUID.randomUUID().toString();

        ChargeSession chargeSession = new ChargeSession();
        chargeSession.setId(sessionId);
        chargeSession.setStationId(stationId);
        chargeSession.setStatus(StatusEnum.IN_PROGRESS);
        chargeSession.setStartedAt(currentTime);

        sessions.put(sessionId, chargeSession);

        startTimeSessionIds.put(currentTime, sessionId);
        totalTimeSessionIds.put(currentTime, sessionId);

        return chargeSession;
    }

    public ChargeSession stopCharging(String id) {
        LocalDateTime currentTime = LocalDateTime.now();

        ChargeSession chargeSession = sessions.get(id);
        chargeSession.setStatus(StatusEnum.FINISHED);
        chargeSession.setStoppedAt(currentTime);

        sessions.put(id, chargeSession);

        stopTimeSessionIds.put(currentTime, id);
        totalTimeSessionIds.remove(chargeSession.getStartedAt());
        totalTimeSessionIds.put(currentTime, id);

        return chargeSession;
    }

    public List<ChargeSession> retrieveSessions() {
        return new ArrayList<>(sessions.values());
    }

    public SummaryResponse retrieveSummary() {
        LocalDateTime minuteAgoTime = LocalDateTime.now().minusMinutes(1L);

        SummaryResponse summaryResponse = new SummaryResponse();
        summaryResponse.setTotalCount(totalTimeSessionIds.tailMap(minuteAgoTime).size());
        summaryResponse.setStartedCount(startTimeSessionIds.tailMap(minuteAgoTime).size());
        summaryResponse.setStoppedCount(stopTimeSessionIds.tailMap(minuteAgoTime).size());

        return summaryResponse;
    }
}

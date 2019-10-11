package com.example.chargestation.repository;

import com.example.chargestation.entity.ChargeSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 */
public class SessionRepositoryImpl implements SessionRepository {

    private Map<String, ChargeSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void save(ChargeSession chargeSession) {
        sessions.put(chargeSession.getId(), chargeSession);
    }

    @Override
    public List<ChargeSession> findAll() {
        return new ArrayList<>(sessions.values());
    }

    @Override
    public ChargeSession findById(String stationId) {
        return sessions.get(stationId);
    }
}

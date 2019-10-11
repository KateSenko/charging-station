package com.example.chargestation.repository;

import com.example.chargestation.entity.ChargeSession;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository {

    void save(ChargeSession chargeSession);

    List<ChargeSession> findAll();

    ChargeSession findById(String stationId);
}

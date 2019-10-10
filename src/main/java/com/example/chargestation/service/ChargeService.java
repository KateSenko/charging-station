package com.example.chargestation.service;

import com.example.chargestation.entity.ChargeSession;
import com.example.chargestation.entity.SummaryResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The interface Charge service.
 */
@Service
public interface ChargeService {
    /**
     * Start session charge session.
     *
     * @param stationId the station id
     * @return the charge session
     */
    ChargeSession startSession(String stationId);

    /**
     * Stop charging charge session.
     *
     * @param id the id
     * @return the charge session
     */
    ChargeSession stopCharging(String id);

    /**
     * Retrieve sessions list.
     *
     * @return the list
     */
    List<ChargeSession> retrieveSessions();

    /**
     * Retrieve summary summary response.
     *
     * @return the summary response
     */
    SummaryResponse retrieveSummary();
}

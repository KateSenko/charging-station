package com.example.chargestation.service;

import com.example.chargestation.entity.ChargeSession;
import com.example.chargestation.controller.response.SummaryResponse;
import com.example.chargestation.exception.SessionNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChargeService {

    ChargeSession startCharging(String stationId);

    ChargeSession stopCharging(String id) throws SessionNotFoundException;

    List<ChargeSession> retrieveSessions();

    SummaryResponse retrieveSummary();
}

package com.example.chargestation.controller.response;

import com.example.chargestation.entity.ChargeSession;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
@Component
public class ChargeSessionResponseConverter {

    public ChargeSessionResponse createFrom(ChargeSession chargeSession) {
        if (chargeSession == null)
            return null;

        ChargeSessionResponse chargeSessionResponse = new ChargeSessionResponse();

        chargeSessionResponse.setId(chargeSession.getId());
        chargeSessionResponse.setStationId(chargeSession.getStationId());
        if (chargeSession.getStartedAt() != null) {
            chargeSessionResponse.setStartedAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(chargeSession.getStartedAt()), ZoneId.systemDefault()));
        }
        if (chargeSession.getStoppedAt() != null)
            chargeSessionResponse.setStoppedAt(LocalDateTime.ofInstant(Instant.ofEpochMilli(chargeSession.getStoppedAt()), ZoneId.systemDefault()));
        chargeSessionResponse.setStatus(chargeSession.getStatus());

        return chargeSessionResponse;
    }

    public List<ChargeSessionResponse> createFrom(List<ChargeSession> chargeSessions) {
        if (CollectionUtils.isEmpty(chargeSessions)) {
            return Collections.emptyList();
        }

        List<ChargeSessionResponse> chargeSessionResponses = new ArrayList<>();
        chargeSessions.forEach(chargeSession -> chargeSessionResponses.add(createFrom(chargeSession)));

        return chargeSessionResponses;
    }
}

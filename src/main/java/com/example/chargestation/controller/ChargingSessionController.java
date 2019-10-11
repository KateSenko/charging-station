package com.example.chargestation.controller;

import com.example.chargestation.controller.response.ChargeSessionResponse;
import com.example.chargestation.controller.request.ChargeSessionRequest;
import com.example.chargestation.controller.response.ChargeSessionResponseConverter;
import com.example.chargestation.controller.response.SummaryResponse;
import com.example.chargestation.entity.ChargeSession;
import com.example.chargestation.exception.SessionNotFoundException;
import com.example.chargestation.service.ChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/chargingSessions")
public class ChargingSessionController {

    @Autowired
    private ChargeService chargeService;
    @Autowired
    private ChargeSessionResponseConverter chargeSessionResponseConverter;

    @PostMapping
    public ChargeSessionResponse startCharging(@RequestBody ChargeSessionRequest chargeSessionRequest) {
        ChargeSession chargeSession = chargeService.startCharging(chargeSessionRequest.getStationId());
        return chargeSessionResponseConverter.createFrom(chargeSession);
    }

    @PutMapping(value = "/{id}")
    public ChargeSessionResponse stopCharging(@PathVariable("id") String id) throws SessionNotFoundException {
        ChargeSession chargeSession = chargeService.stopCharging(id);
        return chargeSessionResponseConverter.createFrom(chargeSession);
    }

    @GetMapping
    public List<ChargeSessionResponse> retrieveCharges(){
        List<ChargeSession> chargeSessions = chargeService.retrieveSessions();
        return chargeSessionResponseConverter.createFrom(chargeSessions);
    }

    @GetMapping(value = "summary")
    public SummaryResponse retrieveChargeSummery() {
        return chargeService.retrieveSummary();
    }
}

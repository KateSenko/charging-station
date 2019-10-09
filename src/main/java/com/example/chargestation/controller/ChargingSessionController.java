package com.example.chargestation.controller;

import com.example.chargestation.entity.ChargeSession;
import com.example.chargestation.entity.SummaryResponse;
import com.example.chargestation.service.ChargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/chargingSessions")
public class ChargingSessionController {

    @Autowired
    private ChargeService chargeService;

    @PostMapping
    public ChargeSession startCharging(@RequestBody ChargeSession chargeSessionRequest) {
        ChargeSession chargeSession = chargeService.startSession(chargeSessionRequest.getStationId());
        return chargeSession;
    }

    @PutMapping(value = "/{id}")
    public ChargeSession stopCharging(@PathVariable("id") String id){
        return chargeService.stopCharging(id);
    }

    @GetMapping
    public List<ChargeSession> retrieveCharges(){
        return chargeService.retrieveSessions();
    }

    @GetMapping(value = "summary")
    public SummaryResponse retrieveChargeSummery() {
        return chargeService.retrieveSummary();
    }
}

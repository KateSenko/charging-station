package com.example.chargestation.controller;

import com.example.chargestation.entity.ChargeSession;
import com.example.chargestation.entity.SummaryResponse;
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

    @PostMapping
    public ChargeSession startCharging(@RequestBody ChargeSession chargeSessionRequest) {
        return chargeService.startCharging(chargeSessionRequest.getStationId());
    }

    @PutMapping(value = "/{id}")
    public ChargeSession stopCharging(@PathVariable("id") String id) throws SessionNotFoundException {
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

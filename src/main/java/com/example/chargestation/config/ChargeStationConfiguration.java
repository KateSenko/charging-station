package com.example.chargestation.config;

import com.example.chargestation.repository.SessionRepository;
import com.example.chargestation.repository.SessionRepositoryImpl;
import com.example.chargestation.service.ChargeService;
import com.example.chargestation.service.ChargeServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChargeStationConfiguration {

    @Bean
    SessionRepository sessionRepository() {
        return new SessionRepositoryImpl();
    }

    @Bean
    public ChargeService chargeService(){
        return new ChargeServiceImpl();
    }

}

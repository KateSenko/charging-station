package com.example.chargestation.config;

import com.example.chargestation.service.ChargeService;
import com.example.chargestation.service.ChargeServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChargeStationConfiguration {

    @Bean
    public ChargeService chargeService(){
        return new ChargeServiceImpl();
    }

}

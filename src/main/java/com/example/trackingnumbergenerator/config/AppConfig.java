package com.example.trackingnumbergenerator.config;

import com.example.trackingnumbergenerator.service.TrackingNumberService;
import com.example.trackingnumbergenerator.util.SnowflakeIdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public SnowflakeIdGenerator snowflakeIdGenerator() {
        return TrackingNumberService.createGenerator();
    }

    @Bean
    public TrackingNumberService trackingNumberService(SnowflakeIdGenerator idGenerator) {
        return new TrackingNumberService(idGenerator);
    }
}
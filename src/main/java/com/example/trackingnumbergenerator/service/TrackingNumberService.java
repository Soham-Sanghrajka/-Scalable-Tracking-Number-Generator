package com.example.trackingnumbergenerator.service;

import com.example.trackingnumbergenerator.dto.TrackingNumberRequest;
import com.example.trackingnumbergenerator.dto.TrackingNumberResponse;
import com.example.trackingnumbergenerator.exception.TrackingNumberGenerationException;
import com.example.trackingnumbergenerator.util.SnowflakeIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;

@Service
public class TrackingNumberService {
    private static final Logger logger = LoggerFactory.getLogger(TrackingNumberService.class);
    private final SnowflakeIdGenerator idGenerator;

    // Use constructor injection for testability
    public TrackingNumberService(SnowflakeIdGenerator idGenerator) {
        this.idGenerator = idGenerator;
    }

    public static SnowflakeIdGenerator createGenerator() {
        try {
            long nodeId = Long.parseLong(System.getenv().getOrDefault("NODE_ID", "0"));
            return new SnowflakeIdGenerator(nodeId);
        } catch (NumberFormatException e) {
            logger.error("Invalid NODE_ID format. Must be a number", e);
            throw new TrackingNumberGenerationException("Invalid node configuration");
        }
    }

    public Mono<TrackingNumberResponse> generateTrackingNumber(TrackingNumberRequest request) {
        return Mono.fromCallable(() -> {
                    try {
                        long id = idGenerator.nextId();
                        String trackingNumber = Long.toString(id, 36).toUpperCase();

                        if (!trackingNumber.matches("^[A-Z0-9]{1,16}$")) {
                            throw new TrackingNumberGenerationException("Generated invalid tracking number format");
                        }

                        return new TrackingNumberResponse(trackingNumber, ZonedDateTime.now());
                    } catch (Exception e) {
                        logger.error("Tracking number generation failed", e);
                        throw new TrackingNumberGenerationException("Failed to generate tracking number", e);
                    }
                })
                .onErrorMap(e -> new TrackingNumberGenerationException("Tracking number service unavailable", e));
    }
}

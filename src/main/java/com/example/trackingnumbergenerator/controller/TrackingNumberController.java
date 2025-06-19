package com.example.trackingnumbergenerator.controller;

import com.example.trackingnumbergenerator.dto.TrackingNumberRequest;
import com.example.trackingnumbergenerator.dto.TrackingNumberResponse;
import com.example.trackingnumbergenerator.service.TrackingNumberService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class TrackingNumberController {
    private final TrackingNumberService trackingNumberService;

    public TrackingNumberController(TrackingNumberService trackingNumberService) {
        this.trackingNumberService = trackingNumberService;
    }

    @GetMapping("/next-tracking-number")
    public Mono<ResponseEntity<TrackingNumberResponse>> generateTrackingNumber(
            @Valid TrackingNumberRequest request) {
        return trackingNumberService.generateTrackingNumber(request)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.error(e)); // Propagate errors
    }
}
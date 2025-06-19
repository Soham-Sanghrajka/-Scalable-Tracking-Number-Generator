package com.example.trackingnumbergenerator.controller;

import com.example.trackingnumbergenerator.config.AppConfig;
import com.example.trackingnumbergenerator.dto.TrackingNumberResponse;
import com.example.trackingnumbergenerator.exception.TrackingNumberGenerationException;
import com.example.trackingnumbergenerator.service.TrackingNumberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(TrackingNumberController.class)
@Import(AppConfig.class)
class TrackingNumberControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private TrackingNumberService service;

    @Test
    void testGenerateTrackingNumberEndpoint_shouldReturn200() {
        TrackingNumberResponse mockResponse =
                new TrackingNumberResponse("ABC123", ZonedDateTime.now());

        when(service.generateTrackingNumber(any()))
                .thenReturn(Mono.just(mockResponse));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/next-tracking-number")
                        .queryParam("origin_country_id", "IN")
                        .queryParam("destination_country_id", "US")
                        .queryParam("weight", "1.234")
                        .queryParam("customer_id", "de619854-b59b-425e-9db4-943979e1bd49")
                        .queryParam("customer_name", "RedBox Logistics")
                        .queryParam("customer_slug", "redbox-logistics")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.tracking_number").isEqualTo("ABC123");
    }

    @Test
    void testGenerateTrackingNumberEndpoint_shouldReturn400() {
        webTestClient.get()
                .uri("/api/next-tracking-number?origin_country_id=INVALID&destination_country_id=US&weight=1.234")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.status").isEqualTo(400)
                .jsonPath("$.error").isEqualTo("Validation Error");
    }

    @Test
    void testGenerateTrackingNumberEndpoint_serviceError() {
        when(service.generateTrackingNumber(any()))
                .thenReturn(Mono.error(new TrackingNumberGenerationException("Service error")));

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/next-tracking-number")
                        .queryParam("origin_country_id", "IN")
                        .queryParam("destination_country_id", "US")
                        .queryParam("weight", "1.234")
                        .queryParam("customer_id", "de619854-b59b-425e-9db4-943979e1bd49")
                        .queryParam("customer_name", "RedBox Logistics")
                        .queryParam("customer_slug", "redbox-logistics")
                        .build())
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody()
                .jsonPath("$.status").isEqualTo(503)
                .jsonPath("$.error").isEqualTo("Tracking Number Service Error");
    }
}
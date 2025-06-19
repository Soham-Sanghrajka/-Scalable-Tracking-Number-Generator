package com.example.trackingnumbergenerator.service;

import com.example.trackingnumbergenerator.dto.TrackingNumberRequest;
import com.example.trackingnumbergenerator.dto.TrackingNumberResponse;
import com.example.trackingnumbergenerator.exception.TrackingNumberGenerationException;
import com.example.trackingnumbergenerator.util.SnowflakeIdGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrackingNumberServiceTest {

    @Mock
    private SnowflakeIdGenerator mockGenerator;

    @InjectMocks
    private TrackingNumberService service;

    @Test
    void testGenerateTrackingNumber_shouldReturnValidResponse() {
        long testId = 123456789L;
        String expectedTrackingNumber = Long.toString(testId, 36).toUpperCase();

        when(mockGenerator.nextId()).thenReturn(testId);

        Mono<TrackingNumberResponse> responseMono =
                service.generateTrackingNumber(buildReq());

        StepVerifier.create(responseMono)
                .assertNext(response -> {
                    // Use the dynamically calculated value
                    assertThat(response.getTracking_number())
                            .isEqualTo(expectedTrackingNumber)
                            .matches("^[A-Z0-9]{1,16}$");
                    assertThat(response.getCreated_at())
                            .isBeforeOrEqualTo(ZonedDateTime.now());
                })
                .verifyComplete();
    }

    @Test
    void testGenerateTrackingNumber_serviceFailure() {
        when(mockGenerator.nextId()).thenThrow(new RuntimeException("Test exception"));

        Mono<TrackingNumberResponse> response = service.generateTrackingNumber(buildReq());

        StepVerifier.create(response)
                .expectErrorMatches(ex ->
                        ex instanceof TrackingNumberGenerationException
                )
                .verify();
    }

    private TrackingNumberRequest buildReq() {
        TrackingNumberRequest request = new TrackingNumberRequest();
        request.setOrigin_country_id("IN");
        request.setDestination_country_id("US");
        request.setWeight(BigDecimal.valueOf(2.5));
        request.setCustomer_id("de619854-b59b-425e-9db4-943979e1bd49");
        request.setCustomer_name("RedBox Logistics");
        request.setCustomer_slug("redbox-logistics");
        return request;
    }
}
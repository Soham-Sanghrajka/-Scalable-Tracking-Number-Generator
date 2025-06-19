package com.example.trackingnumbergenerator.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import lombok.Data;

@Data
public class TrackingNumberRequest {
    @NotBlank
    @Pattern(regexp = "^[A-Z]{2}$", message = "Must be ISO 3166-1 alpha-2 format")
    private String origin_country_id;

    @NotBlank
    @Pattern(regexp = "^[A-Z]{2}$", message = "Must be ISO 3166-1 alpha-2 format")
    private String destination_country_id;

    @NotNull
    @Positive
    @Digits(integer = 10, fraction = 3)
    private BigDecimal weight;

    @NotBlank
    @org.hibernate.validator.constraints.UUID
    private String customer_id;

    @NotBlank
    private String customer_name;

    @NotBlank
    @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$")
    private String customer_slug;
}

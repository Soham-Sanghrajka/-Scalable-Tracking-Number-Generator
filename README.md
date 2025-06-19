<!-- README.md -->

# Scalable Tracking Number Generator API

# Overview

A high-performance REST API that generates unique tracking numbers for parcel shipments. Built with Spring WebFlux, this solution handles high-concurrency scenarios while ensuring uniqueness and compliance with tracking number format requirements.

# Key Features

-Distributed ID Generation: Snowflake algorithm for collision-resistant IDs
-Reactive Architecture: Non-blocking I/O with Spring WebFlux
-Scalable Design: Horizontal scaling via instance-specific node IDs
-Input Validation: Strict parameter validation (ISO country codes, UUIDs, etc.)
-Comprehensive Error Handling: Structured error responses for all failure scenarios
-Zero Database Dependency: Stateless design for maximum performance

# Technology Stack
Java 17
Spring Boot 3.3.x
Spring WebFlux (Reactive stack)
Project Reactor
JUnit 5 + Mockito for testing
Maven build system


# Installation & Setup
Prerequisites
Java 17 JDK
Maven 3.8+
Environment variable for node configuration

# Steps
Clone repository:
```bash
git clone https://github.com/Soham-Sanghrajka/-Scalable-Tracking-Number-Generator.git
```
Set environment variable:
```bash
# Linux/macOS
export NODE_ID=1

# Windows
set NODE_ID=1
```
Build and run:
```bash
mvn clean package
java -jar target/tracking-number-generator-0.0.1-SNAPSHOT.jar
```

# API Documentation
# Endpoint

```bash
GET /api/next-tracking-number
```

# Parameters

| Name                   | Type    | Required | Format               | Example                          |
|------------------------|---------|----------|----------------------|----------------------------------|
| `origin_country_id`    | string  | Yes      | ISO 3166-1 alpha-2   | `"US"`                           |
| `destination_country_id` | string  | Yes      | ISO 3166-1 alpha-2   | `"IN"`                           |
| `weight`               | decimal | Yes      | ≤3 decimal places     | `1.234`                          |
| `customer_id`          | string  | Yes      | UUID                 | `de619854-b59b-425e-9db4-943979e1bd49` |
| `customer_name`        | string  | Yes      | -                    | `"RedBox Logistics"`             |
| `customer_slug`        | string  | Yes      | kebab-case           | `"redbox-logistics"`             |

# Successful Response
```JSON
{
  "tracking_number": "21I0V9",
  "created_at": "2025-06-18T14:30:00+08:00"
}
```

# Error Responses

-400 Bad Request (Validation errors)
503 Service Unavailable (ID generation failures)

# Test Coverage

Service Layer Tests:

-Valid tracking number generation
-Base36 format compliance
-Exception handling for ID generation failures

Controller Layer Tests:

-Successful request handling (200 OK)
-Parameter validation errors (400 Bad Request)
-Service failure propagation (503 Service Unavailable)

Edge Cases:

-Maximum ID value handling
-Negative ID scenarios
-Invalid node configuration
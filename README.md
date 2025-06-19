<!-- README.md -->
# Tracking Number Generator API

A scalable REST API for generating unique parcel tracking numbers.

## Features
- Snowflake ID algorithm for distributed systems
- Horizontal scalability support
- Input validation
- JSON response format
- Health monitoring endpoint

## Requirements
- Java 17
- Maven
- Environment variable `NODE_ID` for distributed deployment

## Installation
```bash
mvn clean package
java -jar target/tracking-number-generator-0.0.1-SNAPSHOT.jar
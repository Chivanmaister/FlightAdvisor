package com.ivanm.flightadvisor.service.domain;

import lombok.Builder;

@Builder
public record Airport(
    Integer id,
    String name,
    String city,
    String country,
    String iata,
    String icao,
    Double latitude,
    Double longitude,
    Integer altitude,
    String timezone,
    String dst,
    String databaseTimezone,
    String type,
    String source) {}

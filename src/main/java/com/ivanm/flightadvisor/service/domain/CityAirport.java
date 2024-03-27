package com.ivanm.flightadvisor.service.domain;

import lombok.Builder;

@Builder
public record CityAirport (
    Integer id,
    String airportName,
    String cityName,
    String country
) {}

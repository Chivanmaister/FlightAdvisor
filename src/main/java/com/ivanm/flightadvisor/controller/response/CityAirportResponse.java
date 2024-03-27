package com.ivanm.flightadvisor.controller.response;

import lombok.Builder;

@Builder
public record CityAirportResponse(
    Integer id, String airportName, String cityName, String country) {}

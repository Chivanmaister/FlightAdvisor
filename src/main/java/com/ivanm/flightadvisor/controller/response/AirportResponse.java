package com.ivanm.flightadvisor.controller.response;

import lombok.Builder;

@Builder
public record AirportResponse(Integer id, String name, String city, String iata) {}

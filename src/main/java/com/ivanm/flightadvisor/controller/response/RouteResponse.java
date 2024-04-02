package com.ivanm.flightadvisor.controller.response;

import lombok.Builder;

@Builder
public record RouteResponse(
    AirportResponse sourceAirport, AirportResponse destinationAirport, Float price) {}

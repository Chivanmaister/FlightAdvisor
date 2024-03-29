package com.ivanm.flightadvisor.controller.response;

import lombok.Builder;

@Builder
public record RouteResponse(String sourceAirport, String destinationAirport, Float price) {}

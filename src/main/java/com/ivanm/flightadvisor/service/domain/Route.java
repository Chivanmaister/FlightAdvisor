package com.ivanm.flightadvisor.service.domain;

import lombok.Builder;

@Builder
public record Route(
    String airline,
    String airlineId,
    String sourceAirport,
    String sourceAirportId,
    String destinationAirport,
    String destinationAirportId,
    String codeShare,
    String stops,
    String equipment,
    String price) {}

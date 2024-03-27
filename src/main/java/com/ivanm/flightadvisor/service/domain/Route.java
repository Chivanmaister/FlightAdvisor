package com.ivanm.flightadvisor.service.domain;

import lombok.Builder;

@Builder
public record Route(
    String airline,
    String airlineId,
    String sourceAirport,
    Airport sourceAirportId,
    String destinationAirport,
    Airport destinationAirportId,
    String codeShare,
    String stops,
    String equipment,
    String price) {}

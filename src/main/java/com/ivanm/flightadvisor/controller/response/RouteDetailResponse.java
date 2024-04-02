package com.ivanm.flightadvisor.controller.response;

import java.util.List;
import lombok.Builder;

@Builder
public record RouteDetailResponse(List<RouteResponse> routes, Double price) {}

package com.ivanm.flightadvisor.service.domain;

import java.util.List;
import lombok.Builder;

@Builder
public record RouteDetail(List<Route> routes, Double price) {}

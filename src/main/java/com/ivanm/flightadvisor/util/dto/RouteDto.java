package com.ivanm.flightadvisor.util.dto;

import com.ivanm.flightadvisor.dao.entity.RouteEntity;
import com.ivanm.flightadvisor.service.domain.Route;
import java.util.List;

public final class RouteDto {

  public static List<RouteEntity> toEntityList(List<Route> routes) {
    return routes.stream().map(RouteDto::routeEntity).toList();
  }

  public static RouteEntity routeEntity(Route route) {
    return RouteEntity.builder()
        .airline(route.airline())
        .airlineId(route.airlineId())
        .sourceAirport(route.sourceAirport())
        .sourceAirportId(route.sourceAirportId())
        .destinationAirport(route.destinationAirport())
        .destinationAirportId(route.destinationAirportId())
        .codeShare(route.codeShare())
        .stops(route.stops())
        .equipment(route.equipment())
        .price(route.price())
        .build();
  }
}

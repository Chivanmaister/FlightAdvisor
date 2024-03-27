package com.ivanm.flightadvisor.util.dto;

import com.ivanm.flightadvisor.dao.entity.RouteEntity;
import com.ivanm.flightadvisor.exception.ClassInitializationException;
import com.ivanm.flightadvisor.service.domain.Airport;
import com.ivanm.flightadvisor.service.domain.Route;
import java.util.List;
import java.util.Map;

public final class RouteDto {

  private RouteDto() {
    throw new ClassInitializationException("RouteDto cannot be initialized");
  }

  public static List<RouteEntity> toEntities(List<Route> routes, Map<String, Airport> airportMap) {
    return routes.stream().map(route -> RouteDto.toEntity(route, airportMap)).toList();
  }

  public static RouteEntity toEntity(Route route, Map<String, Airport> airportMap) {
    return RouteEntity.builder()
        .airline(route.airline())
        .airlineId(route.airlineId())
        .sourceAirport(route.sourceAirport())
        .sourceAirportId(AirportDto.toEntity(airportMap.get(route.sourceAirport())))
        .destinationAirport(route.destinationAirport())
        .destinationAirportId(AirportDto.toEntity(airportMap.get(route.destinationAirport())))
        .codeShare(route.codeShare())
        .stops(route.stops())
        .equipment(route.equipment())
        .price(route.price())
        .build();
  }
}

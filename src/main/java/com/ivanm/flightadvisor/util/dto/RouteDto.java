package com.ivanm.flightadvisor.util.dto;

import com.ivanm.flightadvisor.controller.response.RouteResponse;
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

  public static List<RouteResponse> toResponses(List<Route> routes) {
    return routes.stream().map(RouteDto::toResponse).toList();
  }

  private static RouteResponse toResponse(Route route) {
    return RouteResponse.builder()
        .sourceAirport(route.sourceAirport())
        .destinationAirport(route.destinationAirport())
        .price(route.price())
        .build();
  }

  public static List<Route> toDomains(List<RouteEntity> routeEntities) {
    return routeEntities.stream().map(RouteDto::toDomain).toList();
  }

  private static Route toDomain(RouteEntity routeEntity) {
    return Route.builder()
        .sourceAirport(routeEntity.getSourceAirport())
        .destinationAirport(routeEntity.getDestinationAirport())
        .price(routeEntity.getPrice())
        .build();
  }
}

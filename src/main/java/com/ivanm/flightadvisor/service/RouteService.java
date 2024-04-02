package com.ivanm.flightadvisor.service;

import com.ivanm.flightadvisor.configuration.SearchRoutesConfiguration;
import com.ivanm.flightadvisor.dao.RouteRepository;
import com.ivanm.flightadvisor.dao.entity.RouteEntity;
import com.ivanm.flightadvisor.exception.NoRoutesFoundException;
import com.ivanm.flightadvisor.service.domain.Airport;
import com.ivanm.flightadvisor.service.domain.Route;
import com.ivanm.flightadvisor.service.domain.RouteDetail;
import com.ivanm.flightadvisor.util.dto.RouteDto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class RouteService {

  private final SearchRoutesConfiguration depthSearchConfig;
  private final RouteRepository repository;
  private final AirportService airportService;

  public void saveRoutes(List<Route> routes, Map<String, Airport> airportMap) {

    List<RouteEntity> routeEntities = RouteDto.toEntities(routes, airportMap);

    save(routeEntities);
  }

  public RouteDetail getLowestRoutePriceBetween(
      Integer sourceAirportId, Integer destinationAirportId) {

    List<List<Route>> routesList = findBetween(sourceAirportId, destinationAirportId);

    return routesList.stream()
        .map(
            routes ->
                RouteDetail.builder()
                    .routes(routes)
                    .price(routes.stream().mapToDouble(Route::price).sum())
                    .build())
        .toList()
        .stream()
        .min(Comparator.comparingDouble(RouteDetail::price))
        .orElseThrow(() -> new NoRoutesFoundException("Cheapest route not found"));
  }

  private List<List<Route>> findBetween(Integer sourceAirportId, Integer destinationAirportId) {

    Airport sourceAirport = airportService.getById(sourceAirportId);
    Airport destinationAirport = airportService.getById(destinationAirportId);

    List<Route> routes = getAllRoutes();
    List<String> passedAirportIatas = new ArrayList<>();
    List<List<Route>> foundRoutes = new LinkedList<>();
    List<Route> passedRoutes = new LinkedList<>();

    passedAirportIatas.add(sourceAirport.iata());
    Map<String, List<Route>> sourceAirportToDestinationRoutes =
        routes.stream().collect(Collectors.groupingBy(Route::sourceAirport));

    searchRoutes(
        sourceAirportToDestinationRoutes,
        sourceAirport.iata(),
        destinationAirport.iata(),
        passedAirportIatas,
        foundRoutes,
        passedRoutes);

    return foundRoutes;
  }

  private void searchRoutes(
      Map<String, List<Route>> sourceAirportToDestinationRoutes,
      String sourceAirportIata,
      String destinationAirportIata,
      List<String> passedAirportIatas,
      List<List<Route>> foundRoutes,
      List<Route> passedRoutes) {

    List<Route> routesWithDestination =
        sourceAirportToDestinationRoutes
            .getOrDefault(sourceAirportIata, Collections.emptyList())
            .stream()
            .filter(route -> !passedAirportIatas.contains(route.destinationAirport()))
            .toList();

    for (Route route : routesWithDestination) {

      if (passedAirportIatas.size() > depthSearchConfig.getDepth()) {
        passedRoutes.remove(route);
        passedAirportIatas.remove(route.destinationAirport());
        break;
      }

      if (route.destinationAirport().equals(destinationAirportIata)) {
        passedRoutes.add(route);
        foundRoutes.add(new LinkedList<>(passedRoutes));
        passedRoutes.remove(route);
        passedAirportIatas.remove(route.sourceAirport());
        passedAirportIatas.remove(route.destinationAirport());
        break;
      }

      passedAirportIatas.add(route.destinationAirport());
      passedRoutes.add(route);
      searchRoutes(
          sourceAirportToDestinationRoutes,
          route.destinationAirport(),
          destinationAirportIata,
          passedAirportIatas,
          foundRoutes,
          passedRoutes);

      passedRoutes.remove(route);
      passedAirportIatas.remove(route.destinationAirport());
    }
  }

  @Transactional
  private void save(List<RouteEntity> routeEntities) {
    repository.saveAll(routeEntities);
  }

  @Transactional(readOnly = true)
  private List<Route> getAllRoutes() {
    return RouteDto.toDomains(repository.findAll());
  }
}

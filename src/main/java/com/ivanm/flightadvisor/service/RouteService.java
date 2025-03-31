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
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.cache.annotation.Cacheable;
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

    List<List<Route>> routesList ;

    switch (depthSearchConfig.getType()) {
      case STANDARD -> routesList = findBetween(sourceAirportId, destinationAirportId);
      case HEURISTIC ->
          routesList = List.of(findCheapestWithHeuristic(sourceAirportId, destinationAirportId));
      default ->
          throw new IllegalStateException("Unexpected value: " + depthSearchConfig.getType());
    }

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

  private List<Route> findCheapestWithHeuristic(
      Integer sourceAirportId, Integer destinationAirportId) {

    Airport sourceAirport = airportService.getById(sourceAirportId);
    Airport destinationAirport = airportService.getById(destinationAirportId);

    var sourceIata = sourceAirport.iata();
    var destinationIata = destinationAirport.iata();

    var sourceToAllCheapestRoutes =
        getAllRoutes().parallelStream()
            .collect(
                Collectors.groupingBy(
                    Route::sourceAirport,
                    Collectors.groupingBy(
                        Route::destinationAirport,
                        Collectors.minBy(Comparator.comparingDouble(Route::price)))))
            .entrySet()
            .parallelStream()
            .collect(
                Collectors.toMap(
                    Entry::getKey,
                    entry ->
                        entry.getValue().values().parallelStream().map(Optional::get).toList()));

    List<Route> destinationRoutes = sourceToAllCheapestRoutes.get(sourceIata);
    List<RoutesWithTotalPrice> savedRoutes =
        destinationRoutes.stream().map(RoutesWithTotalPrice::new).toList();
    RoutesWithTotalPrice cheapestRoutes;
    List<RoutesWithTotalPrice> allSavedRoutes = new LinkedList<>(savedRoutes);

    do {

      cheapestRoutes =
          allSavedRoutes.stream()
              .min(Comparator.comparingDouble(RoutesWithTotalPrice::getTotalPrice))
              .get();
      Route lastRoute = cheapestRoutes.getRoutes().getLast();
      destinationRoutes =
          sourceToAllCheapestRoutes.getOrDefault(lastRoute.destinationAirport(), List.of());
      allSavedRoutes.remove(cheapestRoutes);
      for (Route route : destinationRoutes) {
        if (!cheapestRoutes.getPassedAirports().contains(route.destinationAirport())) {
          RoutesWithTotalPrice newRoute = new RoutesWithTotalPrice(cheapestRoutes, route);
          allSavedRoutes.add(newRoute);
        }
      }

    } while (!cheapestRoutes.getRoutes().stream()
        .map(Route::destinationAirport)
        .toList()
        .contains(destinationIata));

    return cheapestRoutes.getRoutes();
  }

  @Transactional
  private void save(List<RouteEntity> routeEntities) {
    repository.saveAll(routeEntities);
  }

  @Transactional(readOnly = true)
  @Cacheable("routes")
  public List<Route> getAllRoutes() {
    return RouteDto.toDomains(repository.findAll());
  }

  @Getter
  private static class RoutesWithTotalPrice {

    private LinkedList<Route> routes = new LinkedList<>();
    private List<String> passedAirports = new ArrayList<>();
    private double totalPrice;

    public RoutesWithTotalPrice(Route route) {
      passedAirports.add(route.sourceAirport());
      routes.add(route);
      totalPrice += route.price();
    }

    public RoutesWithTotalPrice(RoutesWithTotalPrice clone, Route route) {
      this.routes = new LinkedList<>(clone.routes);
      this.totalPrice = clone.totalPrice;
      this.passedAirports = new LinkedList<>(clone.getPassedAirports());
      addRoute(route);
    }

    public void addRoute(Route route) {
      passedAirports.add(route.destinationAirport());
      routes.add(route);
      totalPrice += route.price();
    }
  }
}

package com.ivanm.flightadvisor.service;

import com.ivanm.flightadvisor.dao.RouteRepository;
import com.ivanm.flightadvisor.dao.entity.RouteEntity;
import com.ivanm.flightadvisor.service.domain.Airport;
import com.ivanm.flightadvisor.service.domain.Route;
import com.ivanm.flightadvisor.util.dto.RouteDto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class RouteService {

  private final RouteRepository repository;
  private final AirportService airportService;

  public void saveRoutes(List<Route> routes, Map<String, Airport> airportMap) {

    List<RouteEntity> routeEntities = RouteDto.toEntities(routes, airportMap);

    save(routeEntities);
  }

  public List<Route> findBetween(Integer sourceAirportId, Integer destinationAirportId) {

    Airport sourceAirport = airportService.getById(sourceAirportId);
    Airport destinationAirport = airportService.getById(destinationAirportId);

    List<Route> routes = getAllRoutes();
    List<String> passedAirportIata = new ArrayList<>();
    List<Route> foundRoutes = new LinkedList<>();

    searchRoutes(routes, sourceAirport.iata(), destinationAirport.iata(), passedAirportIata, foundRoutes);

    return Collections.emptyList();
  }

  private void searchRoutes(
      List<Route> routes,
      String sourceAirportIata,
      String destinationAirportIata,
      List<String> passedAirportIata,
      List<Route> foundRoutes) {

    Map<String, List<Route>> sourceAirportToDestinationRoutes =
        routes.stream().collect(Collectors.groupingBy(Route::sourceAirport));

    List<Route> destinationRoutes = sourceAirportToDestinationRoutes.get(sourceAirportIata);
    //TODO: create recursion
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

package com.ivanm.flightadvisor.service;

import com.ivanm.flightadvisor.configuration.CsvResourceConfiguration;
import com.ivanm.flightadvisor.exception.ReadResourceFileException;
import com.ivanm.flightadvisor.service.domain.Airport;
import com.ivanm.flightadvisor.service.domain.Route;
import com.ivanm.flightadvisor.util.parser.CsvParser;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class InitService {

  private final CsvResourceConfiguration csvResourceConfiguration;
  private final AirportService airportService;
  private final RouteService routeService;

  @PostConstruct
  public void init() {

    List<Airport> airports;
    List<Route> routes;

    Resource airportResource = csvResourceConfiguration.getAirportResource();
    Resource routeResource = csvResourceConfiguration.getRouteResource();

    try (InputStream airportFile = airportResource.getInputStream();
        InputStream  routeFile = routeResource.getInputStream()) {

      airports = CsvParser.toAirports(airportFile);
      routes = CsvParser.toRoutes(routeFile);
    } catch (IOException e) {
      log.error("Error reading stream from resources", e);
      throw new ReadResourceFileException("Unable to read streams from resource");
    }

    List<Airport> filteredAirports =
        airports.stream().filter(airport -> !Objects.equals(airport.iata(), "\\N")).toList();
    Map<String, Airport> airportMap =
        filteredAirports.stream().collect(Collectors.toMap(Airport::iata, airport -> airport));

    airportService.saveAirports(filteredAirports);
    routeService.saveRoutes(routes, airportMap);
  }
}

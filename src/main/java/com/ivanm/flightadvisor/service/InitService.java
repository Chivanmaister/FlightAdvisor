package com.ivanm.flightadvisor.service;

import com.ivanm.flightadvisor.configuration.CsvResourceConfiguration;
import com.ivanm.flightadvisor.exception.ReadResourceFileException;
import com.ivanm.flightadvisor.service.domain.Airport;
import com.ivanm.flightadvisor.service.domain.Route;
import com.ivanm.flightadvisor.util.parser.CsvParser;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
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

    File airportFile;
    File routeFile;

    Resource airportResource = csvResourceConfiguration.getAirportResource();
    Resource routeResource = csvResourceConfiguration.getRouteResource();

    try {
      airportFile = airportResource.getFile();
      routeFile = routeResource.getFile();
    } catch (IOException e) {
      log.error("Error reading files from resources", e);
      throw new ReadResourceFileException("Unable to read files from resource");
    }

    List<Airport> airports = CsvParser.toAirports(airportFile);
    List<Route> routes = CsvParser.toRoutes(routeFile);

    List<Airport> filteredAirports =
        airports.stream().filter(airport -> !Objects.equals(airport.iata(), "\\N")).toList();
    Map<String, Airport> airportMap =
        filteredAirports.stream().collect(Collectors.toMap(Airport::iata, airport -> airport));

    airportService.saveAirports(filteredAirports);
    routeService.saveRoutes(routes, airportMap);
  }
}

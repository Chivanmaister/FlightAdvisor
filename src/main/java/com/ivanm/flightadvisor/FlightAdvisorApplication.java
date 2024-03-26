package com.ivanm.flightadvisor;

import com.ivanm.flightadvisor.dao.AirportRepository;
import com.ivanm.flightadvisor.dao.RouteRepository;
import com.ivanm.flightadvisor.dao.entity.AirportEntity;
import com.ivanm.flightadvisor.dao.entity.RouteEntity;
import com.ivanm.flightadvisor.service.domain.Airport;
import com.ivanm.flightadvisor.service.domain.Route;
import com.ivanm.flightadvisor.util.dto.AirportDto;
import com.ivanm.flightadvisor.util.dto.RouteDto;
import com.ivanm.flightadvisor.util.parser.CsvParser;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootApplication
@EnableJpaRepositories
@EnableTransactionManagement
public class FlightAdvisorApplication {

  @Autowired private RouteRepository routeRepository;
  @Autowired private AirportRepository airportRepository;

  public static void main(String[] args) {

    SpringApplication.run(FlightAdvisorApplication.class, args);
  }

  @Value("classpath:txt/routes.txt")
  public Resource routeResource;

  @Value("classpath:txt/airports.txt")
  public Resource airportResource;

  @PostConstruct
  @Transactional
  public void init() throws IOException {

    List<Airport> airportList = CsvParser.toAirports(airportResource.getFile());
    List<Route> routeList = CsvParser.toRoutes(routeResource.getFile());

    List<Airport> filteredAirports =
        airportList.stream().filter(airport -> !Objects.equals(airport.iata(), "\\N")).toList();
    Map<String, Airport> airportMap =
        filteredAirports.stream().collect(Collectors.toMap(Airport::iata, airport -> airport));

    List<AirportEntity> airportEntities = AirportDto.toEntities(airportList);
    List<RouteEntity> routeEntities = RouteDto.toEntities(routeList, airportMap);

    airportRepository.saveAll(airportEntities);
    routeRepository.saveAll(routeEntities);

    List<RouteEntity> entities = routeRepository.findAll();
  }
}

package com.ivanm.flightadvisor;

import com.ivanm.flightadvisor.dao.RouteRepository;
import com.ivanm.flightadvisor.service.domain.Airport;
import com.ivanm.flightadvisor.service.domain.Route;
import com.ivanm.flightadvisor.util.dto.RouteDto;
import com.ivanm.flightadvisor.util.parser.CsvParser;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
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

  @Autowired
  private RouteRepository routeRepository;

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

    List<Route> routeList = CsvParser.toRoutes(routeResource.getFile());
    List<Airport> airportList = CsvParser.toAirports(airportResource.getFile());

    routeRepository.saveAll(RouteDto.toEntityList(routeList));
  }
}

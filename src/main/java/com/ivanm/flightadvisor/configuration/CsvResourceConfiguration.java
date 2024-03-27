package com.ivanm.flightadvisor.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Getter
@Configuration
public class CsvResourceConfiguration {

  @Value("classpath:txt/routes.txt")
  Resource routeResource;

  @Value("classpath:txt/airports.txt")
  Resource airportResource;
}

package com.ivanm.flightadvisor.configuration;

import jakarta.validation.Valid;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class SearchRoutesConfiguration {

  @Value("${server.search.depth}")
  Integer depth;
}

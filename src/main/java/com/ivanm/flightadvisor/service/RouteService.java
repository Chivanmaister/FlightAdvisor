package com.ivanm.flightadvisor.service;

import com.ivanm.flightadvisor.dao.RouteRepository;
import com.ivanm.flightadvisor.dao.entity.RouteEntity;
import com.ivanm.flightadvisor.service.domain.Airport;
import com.ivanm.flightadvisor.service.domain.Route;
import com.ivanm.flightadvisor.util.dto.RouteDto;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class RouteService {

  private final RouteRepository repository;

  public void saveRoutes(List<Route> routes, Map<String, Airport> airportMap) {

    List<RouteEntity> routeEntities = RouteDto.toEntities(routes, airportMap);

    save(routeEntities);
  }

  @Transactional
  private void save(List<RouteEntity> routeEntities) {
    repository.saveAll(routeEntities);
  }
}

package com.ivanm.flightadvisor.service;

import com.ivanm.flightadvisor.dao.AirportRepository;
import com.ivanm.flightadvisor.dao.entity.AirportEntity;
import com.ivanm.flightadvisor.service.domain.Airport;
import com.ivanm.flightadvisor.util.dto.AirportDto;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AirportService {

  private final AirportRepository repository;

  public void saveAirports(List<Airport> airports) {

    List<AirportEntity> airportEntities = AirportDto.toEntities(airports);

    save(airportEntities);
  }

  @Transactional
  private void save(List<AirportEntity> airportEntities) {
    repository.saveAll(airportEntities);
  }

}

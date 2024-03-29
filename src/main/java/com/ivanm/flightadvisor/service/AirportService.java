package com.ivanm.flightadvisor.service;

import com.ivanm.flightadvisor.dao.AirportRepository;
import com.ivanm.flightadvisor.dao.entity.AirportEntity;
import com.ivanm.flightadvisor.exception.AirportNotFoundException;
import com.ivanm.flightadvisor.service.domain.Airport;
import com.ivanm.flightadvisor.service.domain.CityAirport;
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

  @Transactional(readOnly = true)
  public List<Airport> searchCitiesByName(String name) {
    return AirportDto.toDomains(repository.findAirportByCityName(name));
  }

  @Transactional(readOnly = true)
  public Airport getById(Integer sourceAirportId) {
    return AirportDto.toDomain(
        repository
            .findById(sourceAirportId)
            .orElseThrow(() -> new AirportNotFoundException("Airport doesn't exists")));
  }
}

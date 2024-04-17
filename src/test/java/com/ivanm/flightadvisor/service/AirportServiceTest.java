package com.ivanm.flightadvisor.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ivanm.flightadvisor.dao.AirportRepository;
import com.ivanm.flightadvisor.dao.entity.AirportEntity;
import com.ivanm.flightadvisor.service.domain.Airport;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AirportServiceTest {

  @InjectMocks AirportService airportService;

  @Mock AirportRepository airportRepository;

  @Test
  public void givenListAirports_WhenSaveAirports_ThenSaveAirports() {

    List<Airport> airports = List.of(Airport.builder().build());

    airportService.saveAirports(airports);

    verify(airportRepository, times(1)).saveAll(anyList());
  }

  @Test
  public void givenCityName_WhenCitiesMatchName_ThenReturnCities() {

    String cityName = "cityName";

    when(airportRepository.findAirportByCityName(anyString()))
        .thenReturn(List.of(AirportEntity.builder().city(cityName).build()));

    List<Airport> airports = airportService.searchCitiesByName(cityName);

    assertEquals(1, airports.size());
    assertEquals(cityName, airports.get(0).city());
  }

  @Test
  public void givenCityName_WhenNoCitiesMatch_ThenReturnEmptyList() {

    String cityName = "cityName";

    when(airportRepository.findAirportByCityName(anyString())).thenReturn(List.of());

    List<Airport> airports = airportService.searchCitiesByName(cityName);

    assertEquals(0, airports.size());
  }
}

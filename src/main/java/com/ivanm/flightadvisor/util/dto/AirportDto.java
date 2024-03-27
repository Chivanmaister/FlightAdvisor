package com.ivanm.flightadvisor.util.dto;

import com.ivanm.flightadvisor.dao.entity.AirportEntity;
import com.ivanm.flightadvisor.exception.ClassInitializationException;
import com.ivanm.flightadvisor.service.domain.Airport;
import java.util.List;

public final class AirportDto {

  private AirportDto() {
    throw new ClassInitializationException("AirportDto cannot be initialized");
  }

  public static List<AirportEntity> toEntities(List<Airport> airports) {
    return airports.stream().map(AirportDto::toEntity).toList();
  }

  public static AirportEntity toEntity(Airport airport) {

    if (airport != null) {
      return AirportEntity.builder()
          .id(airport.id())
          .name(airport.name())
          .city(airport.city())
          .country(airport.country())
          .iata(airport.iata())
          .icao(airport.icao())
          .latitude(airport.latitude())
          .longitude(airport.longitude())
          .altitude(airport.altitude())
          .timezone(airport.timezone())
          .dst(airport.dst())
          .databaseTimezone(airport.databaseTimezone())
          .type(airport.type())
          .source(airport.source())
          .build();
    }
    return null;
  }
}

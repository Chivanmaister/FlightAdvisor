package com.ivanm.flightadvisor.util.dto;

import com.ivanm.flightadvisor.controller.response.AirportResponse;
import com.ivanm.flightadvisor.controller.response.CityAirportResponse;
import com.ivanm.flightadvisor.dao.entity.AirportEntity;
import com.ivanm.flightadvisor.exception.ClassInitializationException;
import com.ivanm.flightadvisor.service.domain.Airport;
import com.ivanm.flightadvisor.service.domain.CityAirport;
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

  public static List<Airport> toDomains(List<AirportEntity> airportEntities) {
    return airportEntities.stream().map(AirportDto::toDomain).toList();
  }

  public static Airport toDomain(AirportEntity airport) {
    if (airport == null) {
      return null;
    }

    return Airport.builder()
        .id(airport.id)
        .name(airport.name)
        .city(airport.city)
        .country(airport.country)
        .iata(airport.iata)
        .build();
  }

  public static List<CityAirportResponse> toAirportResponses(List<Airport> airports) {
    return airports.stream().map(AirportDto::toAirportResponse).toList();
  }

  public static CityAirportResponse toAirportResponse(Airport airport) {
    return CityAirportResponse.builder()
        .id(airport.id())
        .airportName(airport.name())
        .cityName(airport.city())
        .country(airport.country())
        .build();
  }

  public static AirportResponse toResponse(Airport airport) {
    return AirportResponse.builder()
        .id(airport.id())
        .name(airport.name())
        .iata(airport.iata())
        .city(airport.city())
        .build();
  }
}

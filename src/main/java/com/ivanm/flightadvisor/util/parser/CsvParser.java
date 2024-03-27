package com.ivanm.flightadvisor.util.parser;

import com.ivanm.flightadvisor.exception.ClassInitializationException;
import com.ivanm.flightadvisor.service.domain.Airport;
import com.ivanm.flightadvisor.service.domain.Route;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

@Slf4j
public final class CsvParser {

  private static final CSVFormat DEFAULT_WITH_ESCAPE =
      CSVFormat.Builder.create(CSVFormat.DEFAULT).setEscape('\\').build();

  public CsvParser() {
    throw new ClassInitializationException("CSVParser cannot be initialized");
  }

  public static List<Airport> toAirports(File path) {

    List<Airport> airports = Collections.emptyList();

    try (CSVParser parser = CSVParser.parse(path, Charset.defaultCharset(), DEFAULT_WITH_ESCAPE)) {
      airports =
          parser.getRecords().stream()
              .map(
                  record ->
                      Airport.builder()
                          .id(Integer.valueOf(record.get(0)))
                          .name(record.get(1))
                          .city(record.get(2))
                          .country(record.get(3))
                          .iata(record.get(4))
                          .icao(record.get(5))
                          .latitude(Double.valueOf(record.get(6)))
                          .longitude(Double.valueOf(record.get(7)))
                          .altitude(Integer.valueOf(record.get(8)))
                          .timezone(record.get(9))
                          .dst(record.get(10))
                          .databaseTimezone(record.get(11))
                          .type(record.get(12))
                          .source(record.get(13))
                          .build())
              .toList();
    } catch (IOException e) {
      log.error("Unable to parse CSV on path: " + path, e);
    }

    return airports;
  }

  public static List<Route> toRoutes(File path) {

    List<Route> routes = Collections.emptyList();

    try (CSVParser parser = CSVParser.parse(path, Charset.defaultCharset(), CSVFormat.DEFAULT)) {
      routes =
          parser.getRecords().stream()
              .map(
                  record ->
                      Route.builder()
                          .airline(record.get(0))
                          .airlineId(record.get(1))
                          .sourceAirport(record.get(2))
                          .sourceAirportId(record.get(3))
                          .destinationAirport(record.get(4))
                          .destinationAirportId(record.get(5))
                          .codeShare(record.get(6))
                          .stops(record.get(7))
                          .equipment(record.get(8))
                          .price(record.get(9))
                          .build())
              .toList();
    } catch (IOException e) {
      log.error("Unable to parse CSV on path: " + path, e);
      //      throw new RuntimeException(e);
    }

    return routes;
  }
}

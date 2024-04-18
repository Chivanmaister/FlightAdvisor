package com.ivanm.flightadvisor.dao;

import static org.hamcrest.MatcherAssert.assertThat;

import com.ivanm.flightadvisor.dao.entity.AirportEntity;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class AirportRepositoryTest {

  @Autowired AirportRepository airportRepository;

  @Test
  public void givenAirportList_WhenFindingAllAirports_ThenReturnAirports() {

    List<AirportEntity> airportEntities = List.of(AirportEntity.builder().id(1).build());
    airportRepository.saveAll(airportEntities);

    List<AirportEntity> airportEntityList = airportRepository.findAll();

    assertThat("Airports not found", !airportEntityList.isEmpty());
  }
}

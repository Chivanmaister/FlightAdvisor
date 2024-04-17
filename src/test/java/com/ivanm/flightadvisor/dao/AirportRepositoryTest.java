package com.ivanm.flightadvisor.dao;

import static org.hamcrest.MatcherAssert.assertThat;

import com.ivanm.flightadvisor.dao.entity.AirportEntity;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

@DataJpaTest
@TestPropertySource(properties = {"spring.test.database.replace=none"})
@Testcontainers
public class AirportRepositoryTest {

  @Autowired AirportRepository airportRepository;

  @Test
  public void given_When_Then() {

    List<AirportEntity> airportEntities = airportRepository.findAll();

    assertThat("Airports not found", !airportEntities.isEmpty());
  }
}

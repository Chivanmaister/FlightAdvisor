package com.ivanm.flightadvisor.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ivanm.flightadvisor.exception.ApplicationExceptionHandler;
import com.ivanm.flightadvisor.service.AirportService;
import com.ivanm.flightadvisor.service.domain.Airport;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = {AirportController.class, ApplicationExceptionHandler.class})
@AutoConfigureMockMvc
@EnableAutoConfiguration(
    exclude = {SecurityAutoConfiguration.class, DataSourceAutoConfiguration.class})
public class AirportControllerTest {

  @MockBean AirportService airportService;
  @Autowired MockMvc mockMvc;

  @Test
  public void givenValidRequest_WhenSearchCities_ThenReturnCities() throws Exception {

    List<Airport> airportList = List.of(Airport.builder().build());

    when(airportService.searchCitiesByName(anyString())).thenReturn(airportList);

    mockMvc
        .perform(get("/airports/cities").param("name", "abc"))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  public void givenNoParam_WhenSearchCities_ThenThrowException() throws Exception {

    mockMvc.perform(get("/airports/cities")).andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  public void givenLongNameParam_WhenSearchCities_ThenThrowException() throws Exception {

    String longNameParam = "abcdefghaikkoewadwoadoadadfjopjof";

    mockMvc
        .perform(get("/airports/cities").param("name", longNameParam))
        .andDo(print())
        .andExpect(status().isBadRequest());
  }
}

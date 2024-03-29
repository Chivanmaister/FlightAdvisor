package com.ivanm.flightadvisor.controller;

import com.ivanm.flightadvisor.controller.response.CityAirportResponse;
import com.ivanm.flightadvisor.service.AirportService;
import com.ivanm.flightadvisor.util.dto.AirportDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/airports")
public class AirportController {

  private final AirportService airportService;

  @GetMapping("/cities")
  public List<CityAirportResponse> searchCities(
      @Valid @RequestParam @NotBlank @Size(max = 15) String name) {
    return AirportDto.toAirportResponses(airportService.searchCitiesByName(name));
  }
}

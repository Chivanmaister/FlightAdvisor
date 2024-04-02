package com.ivanm.flightadvisor.controller;

import com.ivanm.flightadvisor.controller.response.RouteDetailResponse;
import com.ivanm.flightadvisor.service.RouteService;
import com.ivanm.flightadvisor.util.dto.RouteDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/routes")
public class RouteController {

  private final RouteService routeService;

  @GetMapping("/lowestPrice/{fromAirportId}/{toAirportId}")
  public RouteDetailResponse findRoutes(
      @Valid @PathVariable(name = "fromAirportId") Integer sourceAirportId,
      @Valid @PathVariable(name = "toAirportId") Integer destinationAirportId) {

    return RouteDto.toResponse(
        routeService.getLowestRoutePriceBetween(sourceAirportId, destinationAirportId));
  }
}

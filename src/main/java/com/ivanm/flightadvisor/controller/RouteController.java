package com.ivanm.flightadvisor.controller;

import com.ivanm.flightadvisor.controller.response.RouteResponse;
import com.ivanm.flightadvisor.service.RouteService;
import com.ivanm.flightadvisor.util.dto.RouteDto;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/routes/")
public class RouteController {

  private final RouteService routeService;

  @GetMapping("/{fromAirportId}/{toAirportId}")
  public List<RouteResponse> findRoutes(
      @Valid @PathVariable(name = "fromAirportId") Integer sourceAirportId,
      @Valid @PathVariable(name = "toAirportId") Integer destinationAirportId) {

    return RouteDto.toResponses(routeService.findBetween(sourceAirportId, destinationAirportId));
  }
}

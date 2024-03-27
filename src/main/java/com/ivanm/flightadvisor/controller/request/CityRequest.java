package com.ivanm.flightadvisor.controller.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.validation.annotation.Validated;

@Getter
@Validated
public class CityRequest {
  @NotNull @NotBlank @Max(15L) public String name;
}

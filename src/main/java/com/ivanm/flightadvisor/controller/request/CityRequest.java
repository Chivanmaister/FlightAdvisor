package com.ivanm.flightadvisor.controller.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public record CityRequest(@NotNull @NotBlank @Max(15L) String name) {}

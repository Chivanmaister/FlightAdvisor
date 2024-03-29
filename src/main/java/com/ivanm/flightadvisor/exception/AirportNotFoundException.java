package com.ivanm.flightadvisor.exception;

import java.io.Serial;

public class AirportNotFoundException extends ApplicationException {

  @Serial private static final long serialVersionUID = -4518647094221396007L;

  public AirportNotFoundException(String message) {
    super(message);
  }
}

package com.ivanm.flightadvisor.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_IMPLEMENTED, reason = "Class cannot be initialized")
public class ClassInitializationException extends RuntimeException {

  public ClassInitializationException(String message) {
    super(message);
  }
}

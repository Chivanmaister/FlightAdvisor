package com.ivanm.flightadvisor.exception;

import java.io.Serial;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_IMPLEMENTED, reason = "Class cannot be initialized")
public class ClassInitializationException extends RuntimeException {

  @Serial private static final long serialVersionUID = -7809654784277879462L;

  public ClassInitializationException(String message) {
    super(message);
  }
}

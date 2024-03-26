package com.ivanm.flightadvisor.exception;

import java.io.Serial;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class InvalidArgumentException extends ApplicationException {

  @Serial private static final long serialVersionUID = -4895574977216969367L;

  public InvalidArgumentException(String message) {
    super(message);
  }
}

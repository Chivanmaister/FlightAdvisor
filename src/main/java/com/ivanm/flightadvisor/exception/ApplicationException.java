package com.ivanm.flightadvisor.exception;

import java.io.Serial;

public class ApplicationException extends RuntimeException {

  @Serial private static final long serialVersionUID = -6095755989120915099L;

  public ApplicationException(String message) {
    super(message);
  }
}

package com.ivanm.flightadvisor.exception;

import java.io.Serial;

public class ReadResourceFileException extends ApplicationException {

  @Serial private static final long serialVersionUID = 2396922657170898445L;

  public ReadResourceFileException(String message) {
    super(message);
  }
}

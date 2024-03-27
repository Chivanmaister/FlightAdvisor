package com.ivanm.flightadvisor.exception;

import java.io.Serial;

public class CSVParserException extends ApplicationException {

  @Serial private static final long serialVersionUID = 6156300052674403635L;

  public CSVParserException(String message) {
    super(message);
  }
}

package com.ivanm.flightadvisor.exception;

import java.io.Serial;

public class CsvParserException extends ApplicationException {

  @Serial private static final long serialVersionUID = 6156300052674403635L;

  public CsvParserException(String message) {
    super(message);
  }
}

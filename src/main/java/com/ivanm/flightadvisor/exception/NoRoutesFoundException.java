package com.ivanm.flightadvisor.exception;

import java.io.Serial;

public class NoRoutesFoundException extends ApplicationException {

  @Serial private static final long serialVersionUID = -8895114139330835561L;

  public NoRoutesFoundException(String message) {
    super(message);
  }
}

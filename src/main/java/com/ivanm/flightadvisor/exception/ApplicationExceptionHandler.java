package com.ivanm.flightadvisor.exception;

import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationExceptionHandler {

  @ExceptionHandler(ClassInitializationException.class)
  @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
  public ResponseEntity<String> handleClassInitializationException(ClassInitializationException e) {

    return ResponseEntity.of(Optional.of(e.getMessage()));
  }

  @ExceptionHandler(ApplicationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<String> handleApplicationException(ApplicationException e) {

    return ResponseEntity.of(Optional.of(e.getMessage()));
  }
}

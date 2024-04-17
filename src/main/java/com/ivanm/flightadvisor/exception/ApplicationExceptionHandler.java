package com.ivanm.flightadvisor.exception;

import jakarta.validation.ConstraintViolationException;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
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

  @ExceptionHandler(
      value = {ConstraintViolationException.class, MissingServletRequestParameterException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<Map<String, String>> handleViolationException(
      Exception e) {

    Map<String, String> violationMap = Map.of("message", e.getMessage());

    return ResponseEntity.badRequest().body(violationMap);
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<String> handleException(Exception e) {

    log.error(e.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("Something happened unexpectedly");
  }
}

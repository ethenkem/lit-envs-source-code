package com.bookmie.lit.utils.exceptions;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
  private final HttpStatus status;
  private final Object details;

  public ApiException(HttpStatus status, String message) {
    super(message);
    this.status = status;
    this.details = null;
  }

  public ApiException(HttpStatus status, String message, Object details) {
    super(message);
    this.status = status;
    this.details = details;
  }

  public HttpStatus getStatus() {
    return this.status;
  }

  public Object getDetails() {
    return this.details;
  }
}

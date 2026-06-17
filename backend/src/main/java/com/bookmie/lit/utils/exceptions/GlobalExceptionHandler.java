package com.bookmie.lit.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bookmie.lit.utils.dtos.ErrorResponseDto;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<ErrorResponseDto> handleApiException(ApiException ex) {
    ErrorResponseDto errorResponse = new ErrorResponseDto(
        ex.getStatus().value(),
        ex.getMessage(),
        ex.getDetails()
    );
    return new ResponseEntity<>(errorResponse, ex.getStatus());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    ErrorResponseDto errorResponse = new ErrorResponseDto(
        HttpStatus.BAD_REQUEST.value(),
        "Validation failed",
        errors
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponseDto> handleGenericException(Exception ex) {
    ErrorResponseDto errorResponse = new ErrorResponseDto(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        ex.getMessage(),
        getStackTraceAsString(ex)
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private String getStackTraceAsString(Exception ex) {
    StringBuilder sb = new StringBuilder();
    for (StackTraceElement element : ex.getStackTrace()) {
      sb.append(element.toString()).append("\n");
    }
    return sb.toString();
  }
}

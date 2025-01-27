package com.emanuel3k.soundscape_backend.infra.exception;

import com.emanuel3k.soundscape_backend.infra.exception.dto.ExceptionResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ExceptionResponseDTO> handleBadRequestException(BadRequestException ex) {
    ExceptionResponseDTO errors = new ExceptionResponseDTO(ex.getMessage());

    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ExceptionResponseDTO> handleHttpMessageNotReadableException() {
    ExceptionResponseDTO errors = new ExceptionResponseDTO("Invalid request body");

    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ExceptionResponseDTO> handleNoResourceFoundException(NoResourceFoundException ex) {
    ExceptionResponseDTO errors = new ExceptionResponseDTO(ex.getMessage());

    return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
  }
}
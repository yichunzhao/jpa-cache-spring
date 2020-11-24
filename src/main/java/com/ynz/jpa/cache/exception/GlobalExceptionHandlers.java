package com.ynz.jpa.cache.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandlers {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleNotFoundException(ResourceNotFoundException exception) {
        ErrorMessage error = ErrorMessage.of(HttpStatus.NOT_FOUND.value(), LocalDateTime.now(), exception.getMessage(), "Resource is not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}

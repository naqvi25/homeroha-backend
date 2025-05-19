package com.homeroha.exception;

import com.homeroha.exception.HomerohaException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HomerohaException.class)
    public ResponseEntity<?> handleHomerohaError(HomerohaException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
}

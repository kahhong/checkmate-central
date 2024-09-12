package com.ila.checkmatecentral.config;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({ HttpMessageConversionException.class })
    public ResponseEntity<?> handleHttpMessageConversionException(Exception ex) {
        log.error(ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}

package com.jose.curso.springboot.app.springboot_crud.Exceptions;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String,Object>> handlerResourceNotFound(ResourceNotFoundException rnfe){
        Map<String,Object> error = new HashMap<>();
        error.put("date", new Date());
        error.put("error", "No existe el recurso que buscabas");
        error.put("message", rnfe.getMessage());
        error.put("status", HttpStatus.NOT_FOUND.value());

        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(error);
    }
}

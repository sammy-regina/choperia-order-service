package com.choperia.order_system.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Captura especificamente erros de lógica de negócio (como mesa ocupada)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleBusinessError(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    // Captura erros de argumentos inválidos (como mesa não encontrada)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleNotFoundError(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
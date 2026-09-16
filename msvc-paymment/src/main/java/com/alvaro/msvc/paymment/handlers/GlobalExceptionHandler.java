package com.alvaro.msvc.paymment.handlers;

import com.alvaro.msvc.paymment.dto.ErrorException;
import com.alvaro.msvc.paymment.exceptions.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException ex) {

        ErrorException error = new ErrorException(
                ex.getStatus(),
                ex.getMessage()
        );

        return ResponseEntity.status(ex.getStatus()).body(error);
    }
}

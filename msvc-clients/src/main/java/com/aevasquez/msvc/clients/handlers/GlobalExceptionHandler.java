package com.aevasquez.msvc.clients.handlers;

import com.aevasquez.msvc.clients.dto.ErrorResponse;
import com.aevasquez.msvc.clients.dto.ErrorResponseList;
import com.aevasquez.msvc.clients.dto.FieldErrorResponse;
import com.aevasquez.msvc.clients.exceptions.DataIntegrityViolationException;
import com.aevasquez.msvc.clients.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            NotFoundException ex
    ) {
        ErrorResponse response = new ErrorResponse(
                ex.getStatus(),
                ex.getMessage()
        );

        return ResponseEntity
                .status(ex.getStatus())
                .body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseList> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponseList(
                        ex.getStatus(),
                        ex.getMessage(),
                        ex.getFields()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseList> handleValidationException(
            MethodArgumentNotValidException ex
    ) {

        List<FieldErrorResponse> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldErrorResponse(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        ErrorResponseList response =
                new ErrorResponseList(
                        HttpStatus.BAD_REQUEST.value(),
                        "Error de validación",
                        errors
                );

        return ResponseEntity
                .badRequest()
                .body(response);
    }
}

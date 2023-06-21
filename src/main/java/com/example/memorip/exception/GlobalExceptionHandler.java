package com.example.memorip.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldError().getDefaultMessage();
        return new ResponseEntity<>(DefaultRes.res(400, errorMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<DefaultRes<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(DefaultRes.res(400, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<DefaultRes<String>> handleNotFoundException(NotFoundException e) {
        return new ResponseEntity<>(DefaultRes.res(404, e.getMessage()), HttpStatus.NOT_FOUND);
    }

}

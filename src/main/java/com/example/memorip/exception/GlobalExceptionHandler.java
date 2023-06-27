package com.example.memorip.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage;
        FieldError fieldError = ex.getBindingResult().getFieldError();
        if (fieldError != null) {
            errorMessage = fieldError.getDefaultMessage();
        } else {
            errorMessage = "유효성 검사 실패";
        }
        return new ResponseEntity<>(DefaultRes.res(400, errorMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<DefaultRes<String>> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(DefaultRes.res(400, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<DefaultRes<String>> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.error("handleCustomException throw CustomException : {}", errorCode);
        return new ResponseEntity<>(DefaultRes.res(errorCode.getHttpStatus().value(), errorCode.getDetail()), errorCode.getHttpStatus());
    }

}

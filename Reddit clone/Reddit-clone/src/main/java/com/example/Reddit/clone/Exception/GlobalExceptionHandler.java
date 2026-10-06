package com.example.Reddit.clone.Exception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        log.warn(e.getMessage());

        HttpStatus statusCode = HttpStatus.NOT_FOUND;

        ExceptionResponse response = new ExceptionResponse(
                statusCode.value(),
                e.getMessage()
        );

        return ResponseEntity
                .status(statusCode)
                .body(response);
    }


}

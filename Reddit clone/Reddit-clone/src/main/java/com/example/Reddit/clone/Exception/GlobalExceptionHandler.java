package com.example.Reddit.clone.Exception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ExceptionResponse> buildExceptionResponse(HttpStatus status, Exception e) {
         log.warn(e.getMessage());

        ExceptionResponse response = new ExceptionResponse(
                status.value(),
                e.getMessage()
        );

        return ResponseEntity
                .status(status)
                .body(response);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        return buildExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException e) {
        return buildExceptionResponse(HttpStatus.NOT_FOUND, e);
   }

}

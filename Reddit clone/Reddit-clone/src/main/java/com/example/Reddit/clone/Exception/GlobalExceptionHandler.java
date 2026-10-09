package com.example.Reddit.clone.Exception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ExceptionResponse> buildExceptionResponse(HttpStatus status, Exception e) {
         log.error("Exception", e);
        ExceptionResponse response = new ExceptionResponse(
                status.value(),
                e.getMessage()
        );
        return ResponseEntity
                .status(status)
                .body(response);
    }

    // custom message
    private ResponseEntity<ExceptionResponse> buildExceptionResponse(HttpStatus status, Exception e, String message) {
         log.error("Exception", e);
        ExceptionResponse response = new ExceptionResponse(
                status.value(),
                message
        );
        return ResponseEntity
                .status(status)
                .body(response);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception e) {
        return buildExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, e, "Something went wrong");
    }


    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(NotFoundException e) {
        return buildExceptionResponse(HttpStatus.NOT_FOUND, e);
   }


   @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> handleAccessDeniedException(
        AccessDeniedException e
    ) {
        return buildExceptionResponse(HttpStatus.FORBIDDEN, e, "Access denied");
    }

}

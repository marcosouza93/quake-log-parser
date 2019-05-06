package br.com.labs.quakelogparser.http.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AppExceptionHandler {

    private static final String FRIENDLY_MESSAGE =
        "The server encountered an internal error or misconfiguration and was unable to complete your request.";

    @ResponseBody
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<?> handleRuntimeException() {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(FRIENDLY_MESSAGE);
    }

}

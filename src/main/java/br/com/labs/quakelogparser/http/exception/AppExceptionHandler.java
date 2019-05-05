package br.com.labs.quakelogparser.http.exception;

import br.com.labs.quakelogparser.usecase.exception.DataNotFoundByRegexException;
import br.com.labs.quakelogparser.usecase.exception.UnreadableFileException;
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
  @ExceptionHandler(value = DataNotFoundByRegexException.class)
  public ResponseEntity<?> handleDataNotFoundByRegexException() {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(FRIENDLY_MESSAGE);
  }

  @ResponseBody
  @ExceptionHandler(value = UnreadableFileException.class)
  public ResponseEntity<?> handleUnreadableFileException() {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(FRIENDLY_MESSAGE);
  }
}

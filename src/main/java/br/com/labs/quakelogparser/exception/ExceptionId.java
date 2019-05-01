package br.com.labs.quakelogparser.exception;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class ExceptionId implements Serializable {

  private static final long serialVersionUID = -2167231257089167282L;
  private static final String MESSAGE_FORMAT = "[%s] - %s";
  private final String code;
  private final String message;
  private final int httpStatusCode;

  private ExceptionId(final String code, final String message, final int httpStatusCode) {
    this.code = code;
    this.message = message;
    this.httpStatusCode = httpStatusCode;
  }

  public static ExceptionId create(final String code, final String message) {
    return new ExceptionId(code, message, HttpStatus.BAD_REQUEST.value());
  }

  public static ExceptionId internalError() {
    return new ExceptionId(
        HttpStatus.INTERNAL_SERVER_ERROR.name(),
        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
        HttpStatus.INTERNAL_SERVER_ERROR.value());
  }

  public String toString() {
    return String.format(MESSAGE_FORMAT, this.code, this.message);
  }

  public ExceptionId withHttpStatusCode(final int httpStatusCode) {
    return new ExceptionId(this.code, this.message, httpStatusCode);
  }

  public ExceptionId withHttpStatus(final HttpStatus httpStatus) {
    return new ExceptionId(this.code, this.message, httpStatus.value());
  }

  public String code() {
    return this.code;
  }

  public String message() {
    return this.message;
  }

  public int httpStatusCode() {
    return this.httpStatusCode;
  }
}

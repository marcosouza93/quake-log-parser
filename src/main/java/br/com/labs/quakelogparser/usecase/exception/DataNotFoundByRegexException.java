package br.com.labs.quakelogparser.usecase.exception;

public class DataNotFoundByRegexException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private static final String MESSAGE =
      "It was not possible to find a player data using a configured expression.";

  public DataNotFoundByRegexException(final Throwable cause) {
    super(MESSAGE, cause);
  }
}

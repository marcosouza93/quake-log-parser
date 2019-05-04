package br.com.labs.quakelogparser.usecase.exception;

public class UnreadableFileException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private static final String MESSAGE = "It was not possible to read the log file.";

  public UnreadableFileException(final Throwable cause) {
    super(MESSAGE, cause);
  }
}

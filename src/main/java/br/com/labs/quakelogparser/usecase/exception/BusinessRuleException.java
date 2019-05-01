package br.com.labs.quakelogparser.usecase.exception;

import br.com.labs.quakelogparser.exception.ExceptionId;
import br.com.labs.quakelogparser.exception.IdentifiableException;
import org.springframework.http.HttpStatus;

public class BusinessRuleException extends IdentifiableException {

  private static final long serialVersionUID = -2914039952081189989L;

  protected BusinessRuleException(final ExceptionId id) {
    super(id.withHttpStatus(HttpStatus.UNPROCESSABLE_ENTITY));
  }
}

package br.com.labs.quakelogparser.usecase.exception;

import br.com.labs.quakelogparser.exception.ExceptionId;
import org.springframework.http.HttpStatus;

public class DataNotFoundByRegexException extends BusinessRuleException {

    private static final long serialVersionUID = 1L;

    private static final String CODE = "labs.dataNotFoundByRegex";
    private static final String MESSAGE = "It was not possible to find a player data using a configured expression.";

    public DataNotFoundByRegexException() {
        super(ExceptionId.create(CODE, MESSAGE));
    }

    @Override
    public HttpStatus httpStatus() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public int httpStatusCode() {
        return HttpStatus.NOT_FOUND.value();
    }
}

package br.com.labs.quakelogparser.exception;

import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public class IdentifiableException extends RuntimeException {

    private static final long serialVersionUID = 2319108117813027799L;
    private final ExceptionId id;
    private final Map<String, String> headers;

    protected IdentifiableException(final ExceptionId id) {
        super(id.toString());
        this.id = id;
        this.headers = new HashMap();
    }

    public HttpStatus httpStatus() {
        return HttpStatus.valueOf(this.id.httpStatusCode());
    }

    public int httpStatusCode() {
        return this.id.httpStatusCode();
    }

}

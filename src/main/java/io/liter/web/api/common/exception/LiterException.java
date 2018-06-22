package io.liter.web.api.common.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

public class LiterException extends RuntimeException {

    private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    private ErrorMessagerCode errorMessagerCode = ErrorMessagerCode.ERROR;
    private List<LiterError> errors;


    public LiterException(String message) {
        super(message);
    }

    public LiterException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public LiterException(String message, HttpStatus httpStatus, ErrorMessagerCode errorMessagerCode) {
        super(message);
        this.httpStatus = httpStatus;
        this.errorMessagerCode = errorMessagerCode;
    }

    public LiterException(String message, ErrorMessagerCode errorMessagerCode) {
        super(message);
        this.errorMessagerCode = errorMessagerCode;
    }

    public LiterException(ErrorMessagerCode errorMessagerCode) {
        this.errorMessagerCode = errorMessagerCode;
    }


    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    public ErrorMessagerCode getErrorMessagerCode() {
        return this.errorMessagerCode;
    }

    public List<LiterError> getErrors() {
        return this.errors;
    }

    public void setErrors(List<LiterError> errors) {
        this.errors = errors;
    }
}

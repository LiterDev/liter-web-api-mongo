package io.liter.web.api.user.exception;

import io.liter.web.api.common.exception.ErrorMessagerCode;
import io.liter.web.api.common.exception.LiterException;
import org.springframework.http.HttpStatus;

public class PasswordEmptyException extends LiterException {


    public PasswordEmptyException() {
        super("PasswordEmptyException");
    }

    public PasswordEmptyException(String message) {
        super(message);
    }

    public PasswordEmptyException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);

    }

    public PasswordEmptyException(String message, HttpStatus httpStatus, ErrorMessagerCode errorMessagerCode) {
        super(message, httpStatus, errorMessagerCode);
    }

    public PasswordEmptyException(String message, ErrorMessagerCode errorMessagerCode) {
        super(message, errorMessagerCode);
    }

    public PasswordEmptyException(ErrorMessagerCode errorMessagerCode) {
        super(errorMessagerCode);

    }

}

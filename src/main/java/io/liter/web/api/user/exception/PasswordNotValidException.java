package io.liter.web.api.user.exception;

import io.liter.web.api.common.exception.ErrorMessagerCode;
import io.liter.web.api.common.exception.LiterException;
import org.springframework.http.HttpStatus;

public class PasswordNotValidException extends LiterException {


    public PasswordNotValidException() {
        super("PasswordNotValidException");
    }

    public PasswordNotValidException(String message) {
        super(message);
    }

    public PasswordNotValidException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);

    }

    public PasswordNotValidException(String message, HttpStatus httpStatus, ErrorMessagerCode errorMessagerCode) {
        super(message, httpStatus, errorMessagerCode);
    }

    public PasswordNotValidException(String message, ErrorMessagerCode errorMessagerCode) {
        super(message, errorMessagerCode);
    }

    public PasswordNotValidException(ErrorMessagerCode errorMessagerCode) {
        super(errorMessagerCode);

    }

}

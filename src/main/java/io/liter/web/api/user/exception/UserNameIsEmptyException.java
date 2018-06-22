package io.liter.web.api.user.exception;

import io.liter.web.api.common.exception.ErrorMessagerCode;
import io.liter.web.api.common.exception.LiterException;
import org.springframework.http.HttpStatus;

public class UserNameIsEmptyException extends LiterException {


    public UserNameIsEmptyException() {
        super("UsernameIsEmptyException");
    }

    public UserNameIsEmptyException(String message) {
        super(message);
    }

    public UserNameIsEmptyException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);

    }

    public UserNameIsEmptyException(String message, HttpStatus httpStatus, ErrorMessagerCode errorMessagerCode) {
        super(message, httpStatus, errorMessagerCode);
    }

    public UserNameIsEmptyException(String message, ErrorMessagerCode errorMessagerCode) {
        super(message, errorMessagerCode);
    }

    public UserNameIsEmptyException(ErrorMessagerCode errorMessagerCode) {
        super(errorMessagerCode);

    }

}

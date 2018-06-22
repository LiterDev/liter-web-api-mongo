package io.liter.web.api.user.exception;

import io.liter.web.api.common.exception.ErrorMessagerCode;
import io.liter.web.api.common.exception.LiterException;
import org.springframework.http.HttpStatus;

public class UserNameIsAlreadyExistsException extends LiterException {


    public UserNameIsAlreadyExistsException() {
        super("UsernameIsAlreadyExistsException");
    }

    public UserNameIsAlreadyExistsException(String message) {
        super(message);
    }

    public UserNameIsAlreadyExistsException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);

    }

    public UserNameIsAlreadyExistsException(String message, HttpStatus httpStatus, ErrorMessagerCode errorMessagerCode) {
        super(message, httpStatus, errorMessagerCode);
    }

    public UserNameIsAlreadyExistsException(String message, ErrorMessagerCode errorMessagerCode) {
        super(message, errorMessagerCode);
    }

    public UserNameIsAlreadyExistsException(ErrorMessagerCode errorMessagerCode) {
        super(errorMessagerCode);

    }

}

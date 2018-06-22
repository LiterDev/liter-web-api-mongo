package io.liter.web.api.common.exception;

public enum ErrorMessagerCode {

    SUCCESS(200000, "Ok"),
    ERROR(500000, "Fail"),
    USER_PASSWORD_IS_NOT_ALLOWED(500100, "Password is not valid"),
    USER_PASSWORD_IS_EMPTY(500101, "Password is empty"),
    USER_PASSWORD_PATTERN_IS_NOT_ALLOWED(500102, "Password pattern is not allowed."),
    USER_PASSWORD_REPEAT_IS_EMPTY(500103, "Password is not valid"),
    USER_PASSWORD_REPEAT_PATTERN_IS_NOT_ALLOWED(500104, "PasswordRepeat pattern is not allowed."),
    USER_PASSWORD_IS_NOT_EQUALS(500105, "Password is not equals."),
    USER_NAME_IS_EMPTY(500106, "Username is empty"),
    USER_NAME_IS_ALREADY_EXISTS(500107, "Username is already exists"),

    DUMMY(900000, "Dummy");


    ErrorMessagerCode(final int codeValue, final String responseValue) {
        this.codeValue = codeValue;
        this.responseValue = responseValue;
    }

    private int codeValue;
    private String responseValue;

    public int getCode() {
        return codeValue;
    }

    public String getResponseValue() {
        return responseValue;
    }

}

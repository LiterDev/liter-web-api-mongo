package io.liter.web.api.common.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class LiterErrors implements Serializable {
    private int code;
    private String message;
    private List<LiterError> errors = new ArrayList<>();

    @JsonCreator
    public LiterErrors(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public void add(String path, int code, String message) {
        this.errors.add(new LiterError(path, code, message));
    }
}


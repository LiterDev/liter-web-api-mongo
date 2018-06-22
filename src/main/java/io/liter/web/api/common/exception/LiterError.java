package io.liter.web.api.common.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
public class LiterError implements Serializable {
    private String path;
    private int code;
    private String message;

    @JsonCreator
    public LiterError(String path, int code, String message) {
        this.path = path;
        this.code = code;
        this.message = message;
    }

}

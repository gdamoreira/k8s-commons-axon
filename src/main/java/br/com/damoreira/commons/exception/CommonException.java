package br.com.damoreira.commons.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class CommonException extends RuntimeException {

    @Getter
    private String key;

    @Getter
    private String[] params;

    public CommonException(String message) {
        super(message);
    }

    public CommonException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommonException(String message, String key, String... params) {
        super(message);
        this.key = key;
        this.params = params;
    }
}
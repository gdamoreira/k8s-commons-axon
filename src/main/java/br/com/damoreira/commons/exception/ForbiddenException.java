package br.com.damoreira.commons.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ForbiddenException extends CommonException {

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, String key, String... params) {
        super(message, key, params);
    }

}
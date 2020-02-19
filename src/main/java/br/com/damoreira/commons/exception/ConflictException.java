package br.com.damoreira.commons.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ConflictException extends CommonException {

    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(String message, String key, String... params) {
        super(message, key, params);
    }
}
package br.com.damoreira.commons.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class EntityExistsException extends CommonException {

    public EntityExistsException(String message) {
        super(message);
    }

    public EntityExistsException(String message, String key, String... params) {
        super(message, key, params);
    }

}
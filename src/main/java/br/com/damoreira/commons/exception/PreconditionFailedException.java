package br.com.damoreira.commons.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PRECONDITION_FAILED)
public class PreconditionFailedException extends CommonException {

    public PreconditionFailedException(String message) {
        super(message);
    }

    public PreconditionFailedException(String message, String key, String... params) {
        super(message, key, params);
    }

}

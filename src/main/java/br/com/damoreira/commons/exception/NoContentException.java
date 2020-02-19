package br.com.damoreira.commons.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class NoContentException extends CommonException {

    public NoContentException(String message) {
        super(message);
    }

    public NoContentException(String message, String key, String... params) {
        super(message, key, params);
    }

}

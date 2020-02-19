package br.com.damoreira.commons.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends CommonException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, String key, String... params) {
        super(message, key, params);
    }

}

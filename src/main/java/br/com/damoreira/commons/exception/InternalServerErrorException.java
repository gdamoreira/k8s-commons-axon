package br.com.damoreira.commons.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerErrorException extends CommonException {

    public InternalServerErrorException(String message) {
        super(message);
    }

    public InternalServerErrorException(String message, String key, String... params) {
        super(message, key, params);
    }

}

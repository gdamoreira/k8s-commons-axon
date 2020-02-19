package br.com.damoreira.commons.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Objects;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class UnsupportedStateTransitionException extends CommonException {

    private static final long serialVersionUID = 1L;

    public UnsupportedStateTransitionException(Object id, Enum state, Boolean deleted) {
        super("Unsupported state transition. id: " + Objects.toString(id) + " current state: " + state + " deleted: " + deleted);
    }

    public UnsupportedStateTransitionException(String message) {
        super(message);
    }

    public UnsupportedStateTransitionException(String message, String key, String... params) {
        super(message, key, params);
    }

}
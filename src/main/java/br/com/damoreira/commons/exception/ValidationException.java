package br.com.damoreira.commons.exception;

import br.com.damoreira.commons.rest.ErrorResponse.FieldError;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ValidationException extends CommonException {

    private final List<FieldError> errors;

    public ValidationException(Set<ConstraintViolation<Object>> violations) {
        super("One or more constraints were violated.");

        this.errors = Optional.ofNullable(violations)
            .map(v -> v.stream()
                .map(violation -> new FieldError(violation.getMessage(), violation.getPropertyPath().toString()))
                .collect(Collectors.toList())
            )
            .orElse(Collections.emptyList());
    }

    public ValidationException(BindingResult bindingResult) {
        super("One or more constraints were violated.");

        this.errors = Optional.ofNullable(bindingResult)
            .map(Errors::getAllErrors)
            .map(e -> e.stream()
                .filter((error) -> error instanceof org.springframework.validation.FieldError)
                .map(error -> new FieldError(error.getDefaultMessage(), ((org.springframework.validation.FieldError) error).getField()))
                .collect(Collectors.toList())
            )
            .orElse(Collections.emptyList());
    }

    public ValidationException(List<FieldError> errors) {
        super("One or more constraints were violated.");
        this.errors = errors;
    }

}

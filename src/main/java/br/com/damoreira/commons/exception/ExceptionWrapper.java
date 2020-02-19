package br.com.damoreira.commons.exception;

import br.com.damoreira.commons.rest.ErrorResponse.FieldError;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.AggregateNotFoundException;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.List;

@Data
@NoArgsConstructor
public class ExceptionWrapper implements Serializable {

    private String message;

    private String errorKey;

    private String[] errorParams;

    private List<FieldError> errors;

    private StackTraceElement[] stacktrace;

    private String exceptionType;

    public ExceptionWrapper(Throwable throwable) {
        this.message = throwable.getMessage();
        this.stacktrace = throwable.getStackTrace();
        this.exceptionType = throwable.getClass().getName();
    }

    public ExceptionWrapper(CommonException exception) {
        this.message = exception.getMessage();
        this.errorKey = exception.getKey();
        this.errorParams = exception.getParams();
        this.stacktrace = exception.getStackTrace();
        this.exceptionType = exception.getClass().getName();
    }

    public ExceptionWrapper(ValidationException exception) {
        this.message = exception.getMessage();
        this.errors = exception.getErrors();
        this.stacktrace = exception.getStackTrace();
        this.exceptionType = exception.getClass().getName();
    }

    public static ExceptionWrapper fromJson(String json) throws IOException {
        return new ObjectMapper().readValue(json, ExceptionWrapper.class);
    }

    @JsonIgnore
    public Throwable getOriginalException() {
        Throwable exception;
        try {
            Class<?> clazz = Class.forName(this.getExceptionType());

            if (ValidationException.class.equals(clazz)) {
                exception = new ValidationException(this.getErrors());
            } else if (CommonException.class.isAssignableFrom(clazz)) {
                Constructor<?> constructor = clazz.getConstructor(String.class, String.class, String[].class);
                exception = (Throwable) constructor.newInstance(this.getMessage(), this.getErrorKey(), this.getErrorParams());
            } else if (AggregateNotFoundException.class.equals(clazz)) {
                exception = new ResourceNotFoundException(this.getMessage());
            } else {
                Constructor<?> constructor = clazz.getConstructor(String.class);
                exception = (Throwable) constructor.newInstance(this.getMessage());
            }

        } catch (Exception e) {
            exception = new RuntimeException(this.getMessage());
        }

        exception.setStackTrace(this.getStacktrace());

        return exception;
    }

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        try {
            return mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Unable to serialize ExceptionSerializable as Json.", e);
        }
    }


}

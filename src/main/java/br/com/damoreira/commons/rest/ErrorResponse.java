package br.com.damoreira.commons.rest;

import br.com.damoreira.commons.exception.CommonException;
import br.com.damoreira.commons.exception.ValidationException;
import br.com.damoreira.commons.exception.util.StacktraceUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ErrorResponse extends ApiResponse {

    private String errorKey;

    private String[] errorParams;

    private List<FieldError> fieldErrors;

    private String stacktrace;

    public ErrorResponse() {
    }

    protected ErrorResponse(ErrorResponse.Builder builder) {
        super(builder);
        this.errorKey = builder.errorKey;
        this.errorParams = builder.errorParams;
        this.fieldErrors = builder.fieldErrors;
        this.stacktrace = builder.stacktrace;
    }

    public static ErrorResponse.Builder builder() {
        return new ErrorResponse.Builder();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldError {

        private String defaultMessage;

        private String field;

    }

    @Slf4j
    public static class Builder extends ApiResponse.Builder {

        private String errorKey;

        private String[] errorParams;

        private List<FieldError> fieldErrors;

        private String stacktrace;

        public ErrorResponse.Builder timestamp(Date timestamp) {
            super.timestamp(timestamp);
            return this;
        }

        public ErrorResponse.Builder status(Integer status) {
            super.status(status);
            return this;
        }

        public ErrorResponse.Builder message(String message) {
            super.message(message);
            return this;
        }

        public ErrorResponse.Builder body(Object body) {
            super.body(body);
            return this;
        }

        public ErrorResponse.Builder errorKey(String errorKey) {
            this.errorKey = errorKey;
            return this;
        }

        public ErrorResponse.Builder errorParams(String[] errorParams) {
            this.errorParams = errorParams;
            return this;
        }

        public ErrorResponse.Builder fieldErrors(List<FieldError> fieldErrors) {
            this.fieldErrors = fieldErrors;
            return this;
        }

        public ErrorResponse.Builder stacktrace(String stacktrace) {
            this.stacktrace = stacktrace;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(this);
        }

        /**
         * Create {@link ErrorResponse} instance from {@link Throwable}.
         *
         * @param throwable The {@link Throwable} used to create a new {@link ErrorResponse} instance.
         * @return {@link ErrorResponse} new instance
         */
        public ErrorResponse buildFrom(Throwable throwable) {
            HttpStatus status = evalHttpStatus(throwable);
            String message = evalMessage(throwable);

            Builder builder = this.timestamp(new Date())
                .status(status.value())
                .message(message)
                .stacktrace(StacktraceUtil.getStacktrace(throwable));

            if (throwable instanceof CommonException) {
                builder.errorKey(((CommonException) throwable).getKey());
                builder.errorParams(((CommonException) throwable).getParams());
            }

            if (throwable instanceof ValidationException) {
                builder.fieldErrors(((ValidationException) throwable).getErrors());
            }

            return builder.build();
        }

        private String evalMessage(Throwable throwable) {
            return throwable.getMessage();
        }

        private HttpStatus evalHttpStatus(Throwable throwable) {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

            if (throwable.getClass().isAnnotationPresent(ResponseStatus.class)) {
                ResponseStatus annotation = throwable.getClass().getAnnotation(ResponseStatus.class);
                status = annotation.value();
            }

            return status;
        }

    }

}
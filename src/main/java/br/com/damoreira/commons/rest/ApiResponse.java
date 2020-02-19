package br.com.damoreira.commons.rest;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Date;

@Data
public class ApiResponse implements Serializable {

    private Date timestamp;

    private Integer status;

    private String message;

    private Object body;

    public ApiResponse() {
    }

    protected ApiResponse(ApiResponse.Builder builder) {
        this.timestamp = builder.timestamp;
        this.status = builder.status;
        this.message = builder.message;
        this.body = builder.body;
    }

    public static ApiResponse.Builder builder() {
        return new ApiResponse.Builder();
    }

    public static class Builder {

        private Date timestamp = new Date();

        private Integer status;

        private String message;

        private Object body;

        public ApiResponse.Builder timestamp(Date timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public ApiResponse.Builder status(Integer status) {
            this.status = status;
            return this;
        }

        public ApiResponse.Builder message(String message) {
            this.message = message;
            return this;
        }

        public ApiResponse.Builder body(Object body) {
            this.body = body;
            return this;
        }

        public ApiResponse buildSuccess() {
            return status(HttpStatus.OK.value())
                .build();
        }

        public ApiResponse buildSuccess(Object body) {
            return status(HttpStatus.OK.value())
                .body(body)
                .build();
        }

        public ApiResponse build() {
            return new ApiResponse(this);
        }

    }

}
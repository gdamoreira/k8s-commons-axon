package br.com.damoreira.commons.rest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class BulkResponse extends ApiResponse {

    private List<ApiResponse> operations;

    protected BulkResponse(BulkResponse.Builder builder) {
        super(builder);
        this.operations = builder.operations;
    }

    public BulkResponse() {
    }

    public static BulkResponse.Builder builder() {
        Builder builder = new Builder();
        builder.status(HttpStatus.MULTI_STATUS.value());
        builder.message(HttpStatus.MULTI_STATUS.getReasonPhrase());
        return builder;
    }

    public static class Builder extends ApiResponse.Builder {

        private List<ApiResponse> operations;

        public BulkResponse.Builder operations(List<ApiResponse> operations) {
            this.operations = operations;
            return this;
        }

        public BulkResponse build() {
            return new BulkResponse(this);
        }

    }


}
package com.musicplatform.backend_core.shared.exception;

import java.util.List;

public record ApiErrorResponse(
        String message,
        List<FieldError> errors
) {

    public record FieldError(String field, String message) {
    }

    public static ApiErrorResponse of(String message) {
        return new ApiErrorResponse(message, List.of());
    }

    public static ApiErrorResponse of(String message, List<FieldError> errors) {
        return new ApiErrorResponse(message, errors);
    }
}

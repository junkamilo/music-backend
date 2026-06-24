package com.musicplatform.backend_core.auth.exception;

import com.musicplatform.backend_core.shared.exception.ApiException;
import org.springframework.http.HttpStatus;

public class ResendVerificationTooSoonException extends ApiException {

    public ResendVerificationTooSoonException() {
        super("Espera un momento antes de solicitar otro código", HttpStatus.TOO_MANY_REQUESTS);
    }
}

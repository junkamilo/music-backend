package com.musicplatform.backend_core.auth.exception;

import com.musicplatform.backend_core.shared.exception.ApiException;
import org.springframework.http.HttpStatus;

public class VerificationTokenExpiredException extends ApiException {

    public VerificationTokenExpiredException() {
        super("El código de verificación ha expirado", HttpStatus.GONE);
    }
}

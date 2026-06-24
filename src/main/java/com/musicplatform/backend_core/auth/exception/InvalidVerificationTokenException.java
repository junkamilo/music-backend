package com.musicplatform.backend_core.auth.exception;

import com.musicplatform.backend_core.shared.exception.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidVerificationTokenException extends ApiException {

    public InvalidVerificationTokenException() {
        super("Código o token de verificación inválido", HttpStatus.BAD_REQUEST);
    }
}

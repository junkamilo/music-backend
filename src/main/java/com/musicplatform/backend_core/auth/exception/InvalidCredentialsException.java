package com.musicplatform.backend_core.auth.exception;

import com.musicplatform.backend_core.shared.exception.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends ApiException {

    public InvalidCredentialsException() {
        super("Credenciales inválidas", HttpStatus.UNAUTHORIZED);
    }
}

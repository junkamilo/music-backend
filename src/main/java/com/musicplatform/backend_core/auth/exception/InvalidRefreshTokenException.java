package com.musicplatform.backend_core.auth.exception;

import com.musicplatform.backend_core.shared.exception.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidRefreshTokenException extends ApiException {

    public InvalidRefreshTokenException() {
        super("Refresh token inválido o expirado", HttpStatus.UNAUTHORIZED);
    }
}

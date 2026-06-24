package com.musicplatform.backend_core.artist.exception;

import com.musicplatform.backend_core.shared.exception.ApiException;
import org.springframework.http.HttpStatus;

public class ForbiddenProfileAccessException extends ApiException {

    public ForbiddenProfileAccessException() {
        super("No tienes permiso para modificar este perfil", HttpStatus.FORBIDDEN);
    }

    public ForbiddenProfileAccessException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}

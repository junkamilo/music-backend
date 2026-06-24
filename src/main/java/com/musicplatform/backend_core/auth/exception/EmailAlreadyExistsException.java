package com.musicplatform.backend_core.auth.exception;

import com.musicplatform.backend_core.shared.exception.ApiException;
import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends ApiException {

    public EmailAlreadyExistsException(String email) {
        super("Ya existe una cuenta registrada con el correo: " + email, HttpStatus.CONFLICT);
    }
}

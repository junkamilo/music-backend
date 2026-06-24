package com.musicplatform.backend_core.auth.exception;

import com.musicplatform.backend_core.shared.exception.ApiException;
import org.springframework.http.HttpStatus;

public class EmailAlreadyVerifiedException extends ApiException {

    public EmailAlreadyVerifiedException() {
        super("El correo ya está verificado", HttpStatus.CONFLICT);
    }
}

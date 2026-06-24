package com.musicplatform.backend_core.auth.exception;

import com.musicplatform.backend_core.shared.exception.ApiException;
import org.springframework.http.HttpStatus;

public class AccountInactiveException extends ApiException {

    public AccountInactiveException() {
        super("Cuenta inactiva o eliminada", HttpStatus.FORBIDDEN);
    }
}

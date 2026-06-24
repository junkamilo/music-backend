package com.musicplatform.backend_core.user.exception;

import com.musicplatform.backend_core.shared.exception.ApiException;
import org.springframework.http.HttpStatus;

public class NotCreatorException extends ApiException {

    public NotCreatorException() {
        super("Solo los creadores pueden gestionar perfiles de artista", HttpStatus.FORBIDDEN);
    }
}

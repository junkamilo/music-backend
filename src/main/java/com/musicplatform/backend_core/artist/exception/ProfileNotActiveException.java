package com.musicplatform.backend_core.artist.exception;

import com.musicplatform.backend_core.shared.exception.ApiException;
import org.springframework.http.HttpStatus;

public class ProfileNotActiveException extends ApiException {

    public ProfileNotActiveException() {
        super("Perfil de artista no disponible", HttpStatus.NOT_FOUND);
    }
}

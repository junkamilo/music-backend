package com.musicplatform.backend_core.artist.exception;

import com.musicplatform.backend_core.shared.exception.ApiException;
import org.springframework.http.HttpStatus;

public class ArtistProfileAlreadyExistsException extends ApiException {

    public ArtistProfileAlreadyExistsException() {
        super("Ya existe un perfil de artista para este usuario", HttpStatus.CONFLICT);
    }
}

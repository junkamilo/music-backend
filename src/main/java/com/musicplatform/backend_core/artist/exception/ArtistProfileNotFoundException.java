package com.musicplatform.backend_core.artist.exception;

import com.musicplatform.backend_core.shared.exception.ApiException;
import org.springframework.http.HttpStatus;

public class ArtistProfileNotFoundException extends ApiException {

    public ArtistProfileNotFoundException() {
        super("Perfil de artista no encontrado", HttpStatus.NOT_FOUND);
    }
}

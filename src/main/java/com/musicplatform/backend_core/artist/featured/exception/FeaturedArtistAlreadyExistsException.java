package com.musicplatform.backend_core.artist.featured.exception;

import com.musicplatform.backend_core.shared.exception.ApiException;
import org.springframework.http.HttpStatus;

public class FeaturedArtistAlreadyExistsException extends ApiException {

    public FeaturedArtistAlreadyExistsException() {
        super("El artista ya existe en el catálogo destacado", HttpStatus.CONFLICT);
    }
}

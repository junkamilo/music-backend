package com.musicplatform.backend_core.artist.featured.exception;

import com.musicplatform.backend_core.shared.exception.ApiException;
import org.springframework.http.HttpStatus;

public class FeaturedArtistNotFoundException extends ApiException {

    public FeaturedArtistNotFoundException() {
        super("Artista destacado no encontrado", HttpStatus.NOT_FOUND);
    }
}

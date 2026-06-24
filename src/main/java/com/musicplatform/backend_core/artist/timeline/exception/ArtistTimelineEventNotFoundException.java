package com.musicplatform.backend_core.artist.timeline.exception;

import com.musicplatform.backend_core.shared.exception.ApiException;
import org.springframework.http.HttpStatus;

public class ArtistTimelineEventNotFoundException extends ApiException {

    public ArtistTimelineEventNotFoundException() {
        super("Evento de timeline no encontrado", HttpStatus.NOT_FOUND);
    }
}

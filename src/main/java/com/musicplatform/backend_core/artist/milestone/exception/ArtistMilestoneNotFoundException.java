package com.musicplatform.backend_core.artist.milestone.exception;

import com.musicplatform.backend_core.shared.exception.ApiException;
import org.springframework.http.HttpStatus;

public class ArtistMilestoneNotFoundException extends ApiException {

    public ArtistMilestoneNotFoundException() {
        super("Logro no encontrado", HttpStatus.NOT_FOUND);
    }
}

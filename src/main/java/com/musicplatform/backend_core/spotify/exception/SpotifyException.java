package com.musicplatform.backend_core.spotify.exception;

import com.musicplatform.backend_core.shared.exception.ApiException;
import org.springframework.http.HttpStatus;

public class SpotifyException extends ApiException {

    public SpotifyException(String message) {
        super(message, HttpStatus.BAD_GATEWAY);
    }

    public SpotifyException(String message, Throwable cause) {
        super(message, HttpStatus.BAD_GATEWAY, cause);
    }

    public SpotifyException(String message, HttpStatus status, Throwable cause) {
        super(message, status, cause);
    }
}

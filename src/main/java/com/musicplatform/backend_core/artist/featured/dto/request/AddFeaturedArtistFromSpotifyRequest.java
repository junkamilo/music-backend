package com.musicplatform.backend_core.artist.featured.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddFeaturedArtistFromSpotifyRequest(
        @NotBlank(message = "El Spotify ID es obligatorio")
        @Size(max = 100, message = "El Spotify ID no puede superar 100 caracteres")
        String spotifyArtistId
) {
}

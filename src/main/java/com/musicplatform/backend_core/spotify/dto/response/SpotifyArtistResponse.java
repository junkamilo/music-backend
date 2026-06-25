package com.musicplatform.backend_core.spotify.dto.response;

import java.util.List;

public record SpotifyArtistResponse(
        String id,
        String name,
        String imageUrl,
        List<String> genres,
        Integer followers,
        Integer popularity,
        String spotifyUrl
) {
}

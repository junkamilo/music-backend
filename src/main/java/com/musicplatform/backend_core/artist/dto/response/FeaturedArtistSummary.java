package com.musicplatform.backend_core.artist.dto.response;

import com.musicplatform.backend_core.artist.entity.ArtistType;

public record FeaturedArtistSummary(
        Long id,
        String stageName,
        String avatarUrl,
        String spotifyId,
        String spotifyUrl,
        ArtistType artistType
) {
}

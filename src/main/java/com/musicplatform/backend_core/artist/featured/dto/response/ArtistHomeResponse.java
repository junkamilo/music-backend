package com.musicplatform.backend_core.artist.featured.dto.response;

public record ArtistHomeResponse(
        Long id,
        String name,
        String imageUrl,
        String genre,
        String bio,
        HighlightedArtistType type,
        String spotifyUrl,
        String spotifyId,
        String curatorReason,
        Integer spotlightOrder
) {
}

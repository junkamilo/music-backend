package com.musicplatform.backend_core.artist.featured.dto.response;

public record FeaturedArtistSyncResult(
        int synced,
        int failed
) {
}

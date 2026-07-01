package com.musicplatform.backend_core.artist.featured.dto.response;

import com.musicplatform.backend_core.artist.featured.entity.SyncStatus;
import java.time.LocalDateTime;

public record FeaturedArtistAdminResponse(
        Long id,
        String stageName,
        String spotifyId,
        SyncStatus syncStatus,
        LocalDateTime lastSyncedAt
) {
}

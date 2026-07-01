package com.musicplatform.backend_core.artist.featured.entity;

import java.time.LocalDateTime;

public final class SpotifyTopArtistSeedSetters {

    private SpotifyTopArtistSeedSetters() {
    }

    public static void setSpotifyId(SpotifyTopArtistSeed seed, String spotifyId) {
        seed.spotifyId = spotifyId;
    }

    public static void setStageName(SpotifyTopArtistSeed seed, String stageName) {
        seed.stageName = stageName;
    }

    public static void setLastSyncedAt(SpotifyTopArtistSeed seed, LocalDateTime lastSyncedAt) {
        seed.lastSyncedAt = lastSyncedAt;
    }

    public static void setSyncPriority(SpotifyTopArtistSeed seed, Integer syncPriority) {
        seed.syncPriority = syncPriority;
    }
}

package com.musicplatform.backend_core.artist.featured.entity;

import java.time.LocalDateTime;

public final class SpotifyTopArtistSeedGetters {

    private SpotifyTopArtistSeedGetters() {
    }

    public static Long getId(SpotifyTopArtistSeed seed) {
        return seed.id;
    }

    public static String getSpotifyId(SpotifyTopArtistSeed seed) {
        return seed.spotifyId;
    }

    public static String getStageName(SpotifyTopArtistSeed seed) {
        return seed.stageName;
    }

    public static LocalDateTime getLastSyncedAt(SpotifyTopArtistSeed seed) {
        return seed.lastSyncedAt;
    }

    public static Integer getSyncPriority(SpotifyTopArtistSeed seed) {
        return seed.syncPriority;
    }
}

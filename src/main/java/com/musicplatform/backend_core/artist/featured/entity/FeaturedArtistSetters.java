package com.musicplatform.backend_core.artist.featured.entity;

import java.time.LocalDateTime;

public final class FeaturedArtistSetters {

    private FeaturedArtistSetters() {
    }

    public static void setSpotifyId(FeaturedArtist artist, String spotifyId) {
        artist.spotifyId = spotifyId;
    }

    public static void setStageName(FeaturedArtist artist, String stageName) {
        artist.stageName = stageName;
    }

    public static void setImageUrl(FeaturedArtist artist, String imageUrl) {
        artist.imageUrl = imageUrl;
    }

    public static void setSpotifyUrl(FeaturedArtist artist, String spotifyUrl) {
        artist.spotifyUrl = spotifyUrl;
    }

    public static void setPopularity(FeaturedArtist artist, Integer popularity) {
        artist.popularity = popularity;
    }

    public static void setGenres(FeaturedArtist artist, String genres) {
        artist.genres = genres;
    }

    public static void setSyncStatus(FeaturedArtist artist, SyncStatus syncStatus) {
        artist.syncStatus = syncStatus;
    }

    public static void setLastSyncedAt(FeaturedArtist artist, LocalDateTime lastSyncedAt) {
        artist.lastSyncedAt = lastSyncedAt;
    }
}

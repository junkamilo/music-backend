package com.musicplatform.backend_core.artist.featured.entity;

import java.time.LocalDateTime;

public final class FeaturedArtistGetters {

    private FeaturedArtistGetters() {
    }

    public static Long getId(FeaturedArtist artist) {
        return artist.id;
    }

    public static String getSpotifyId(FeaturedArtist artist) {
        return artist.spotifyId;
    }

    public static String getStageName(FeaturedArtist artist) {
        return artist.stageName;
    }

    public static String getImageUrl(FeaturedArtist artist) {
        return artist.imageUrl;
    }

    public static String getSpotifyUrl(FeaturedArtist artist) {
        return artist.spotifyUrl;
    }

    public static Integer getPopularity(FeaturedArtist artist) {
        return artist.popularity;
    }

    public static String getGenres(FeaturedArtist artist) {
        return artist.genres;
    }

    public static SyncStatus getSyncStatus(FeaturedArtist artist) {
        return artist.syncStatus;
    }

    public static LocalDateTime getLastSyncedAt(FeaturedArtist artist) {
        return artist.lastSyncedAt;
    }

    public static LocalDateTime getCreatedAt(FeaturedArtist artist) {
        return artist.createdAt;
    }

    public static LocalDateTime getUpdatedAt(FeaturedArtist artist) {
        return artist.updatedAt;
    }
}

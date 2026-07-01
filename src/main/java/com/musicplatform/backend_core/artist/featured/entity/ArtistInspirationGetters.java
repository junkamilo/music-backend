package com.musicplatform.backend_core.artist.featured.entity;

import java.time.LocalDateTime;

public final class ArtistInspirationGetters {

    private ArtistInspirationGetters() {
    }

    public static Long getId(ArtistInspiration inspiration) {
        return inspiration.id;
    }

    public static Long getEmergentArtistId(ArtistInspiration inspiration) {
        return inspiration.emergentArtistId;
    }

    public static Long getFeaturedArtistId(ArtistInspiration inspiration) {
        return inspiration.featuredArtistId;
    }

    public static String getSimilarityReason(ArtistInspiration inspiration) {
        return inspiration.similarityReason;
    }

    public static LocalDateTime getCreatedAt(ArtistInspiration inspiration) {
        return inspiration.createdAt;
    }
}

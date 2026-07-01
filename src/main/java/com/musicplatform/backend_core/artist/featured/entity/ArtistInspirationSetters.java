package com.musicplatform.backend_core.artist.featured.entity;

public final class ArtistInspirationSetters {

    private ArtistInspirationSetters() {
    }

    public static void setEmergentArtistId(ArtistInspiration inspiration, Long emergentArtistId) {
        inspiration.emergentArtistId = emergentArtistId;
    }

    public static void setFeaturedArtistId(ArtistInspiration inspiration, Long featuredArtistId) {
        inspiration.featuredArtistId = featuredArtistId;
    }

    public static void setSimilarityReason(ArtistInspiration inspiration, String similarityReason) {
        inspiration.similarityReason = similarityReason;
    }
}

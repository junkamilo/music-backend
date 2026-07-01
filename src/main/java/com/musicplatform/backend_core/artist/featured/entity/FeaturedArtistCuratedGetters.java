package com.musicplatform.backend_core.artist.featured.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class FeaturedArtistCuratedGetters {

    private FeaturedArtistCuratedGetters() {
    }

    public static Long getId(FeaturedArtistCurated curated) {
        return curated.id;
    }

    public static FeaturedArtist getFeaturedArtist(FeaturedArtistCurated curated) {
        return curated.featuredArtist;
    }

    public static String getCuratorReason(FeaturedArtistCurated curated) {
        return curated.curatorReason;
    }

    public static Integer getSpotlightOrder(FeaturedArtistCurated curated) {
        return curated.spotlightOrder;
    }

    public static boolean isActive(FeaturedArtistCurated curated) {
        return curated.active;
    }

    public static LocalDate getFeaturedFrom(FeaturedArtistCurated curated) {
        return curated.featuredFrom;
    }

    public static LocalDate getFeaturedUntil(FeaturedArtistCurated curated) {
        return curated.featuredUntil;
    }

    public static LocalDateTime getCreatedAt(FeaturedArtistCurated curated) {
        return curated.createdAt;
    }

    public static LocalDateTime getUpdatedAt(FeaturedArtistCurated curated) {
        return curated.updatedAt;
    }
}

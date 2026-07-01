package com.musicplatform.backend_core.artist.featured.entity;

import java.time.LocalDate;

public final class FeaturedArtistCuratedSetters {

    private FeaturedArtistCuratedSetters() {
    }

    public static void setFeaturedArtist(FeaturedArtistCurated curated, FeaturedArtist featuredArtist) {
        curated.featuredArtist = featuredArtist;
    }

    public static void setCuratorReason(FeaturedArtistCurated curated, String curatorReason) {
        curated.curatorReason = curatorReason;
    }

    public static void setSpotlightOrder(FeaturedArtistCurated curated, Integer spotlightOrder) {
        curated.spotlightOrder = spotlightOrder;
    }

    public static void setActive(FeaturedArtistCurated curated, boolean active) {
        curated.active = active;
    }

    public static void setFeaturedFrom(FeaturedArtistCurated curated, LocalDate featuredFrom) {
        curated.featuredFrom = featuredFrom;
    }

    public static void setFeaturedUntil(FeaturedArtistCurated curated, LocalDate featuredUntil) {
        curated.featuredUntil = featuredUntil;
    }
}

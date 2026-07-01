package com.musicplatform.backend_core.artist.featured.entity;

public final class FeaturedArtistFactory {

    private FeaturedArtistFactory() {
    }

    public static FeaturedArtist createEmpty() {
        return new FeaturedArtist();
    }
}

package com.musicplatform.backend_core.artist.entity;

public final class ArtistProfileFactory {

    private ArtistProfileFactory() {
    }

    public static ArtistProfile createEmpty() {
        return new ArtistProfile();
    }
}

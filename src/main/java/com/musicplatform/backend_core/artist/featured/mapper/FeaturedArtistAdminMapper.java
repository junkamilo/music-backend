package com.musicplatform.backend_core.artist.featured.mapper;

import com.musicplatform.backend_core.artist.featured.dto.response.FeaturedArtistAdminResponse;
import com.musicplatform.backend_core.artist.featured.entity.FeaturedArtist;
import com.musicplatform.backend_core.artist.featured.entity.FeaturedArtistGetters;

public final class FeaturedArtistAdminMapper {

    private FeaturedArtistAdminMapper() {
    }

    public static FeaturedArtistAdminResponse toAdminResponse(FeaturedArtist artist) {
        return new FeaturedArtistAdminResponse(
                FeaturedArtistGetters.getId(artist),
                FeaturedArtistGetters.getStageName(artist),
                FeaturedArtistGetters.getSpotifyId(artist),
                FeaturedArtistGetters.getSyncStatus(artist),
                FeaturedArtistGetters.getLastSyncedAt(artist)
        );
    }
}

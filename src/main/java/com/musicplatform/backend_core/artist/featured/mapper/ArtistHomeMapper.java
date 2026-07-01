package com.musicplatform.backend_core.artist.featured.mapper;

import com.musicplatform.backend_core.artist.entity.ArtistProfile;
import com.musicplatform.backend_core.artist.entity.ArtistProfileGetters;
import com.musicplatform.backend_core.artist.featured.dto.response.ArtistHomeResponse;
import com.musicplatform.backend_core.artist.featured.dto.response.HighlightedArtistType;
import com.musicplatform.backend_core.artist.featured.entity.FeaturedArtist;
import com.musicplatform.backend_core.artist.featured.entity.FeaturedArtistCurated;
import com.musicplatform.backend_core.artist.featured.entity.FeaturedArtistCuratedGetters;
import com.musicplatform.backend_core.artist.featured.entity.FeaturedArtistGetters;

public final class ArtistHomeMapper {

    private ArtistHomeMapper() {
    }

    public static ArtistHomeResponse fromFeatured(FeaturedArtist featured) {
        return new ArtistHomeResponse(
                FeaturedArtistGetters.getId(featured),
                FeaturedArtistGetters.getStageName(featured),
                FeaturedArtistGetters.getImageUrl(featured),
                resolveDisplayGenre(FeaturedArtistGetters.getGenres(featured)),
                null,
                HighlightedArtistType.FEATURED,
                FeaturedArtistGetters.getSpotifyUrl(featured),
                FeaturedArtistGetters.getSpotifyId(featured),
                null,
                null
        );
    }

    public static ArtistHomeResponse fromCurated(FeaturedArtistCurated curated) {
        FeaturedArtist featured = FeaturedArtistCuratedGetters.getFeaturedArtist(curated);
        return new ArtistHomeResponse(
                FeaturedArtistGetters.getId(featured),
                FeaturedArtistGetters.getStageName(featured),
                FeaturedArtistGetters.getImageUrl(featured),
                resolveDisplayGenre(FeaturedArtistGetters.getGenres(featured)),
                null,
                HighlightedArtistType.FEATURED,
                FeaturedArtistGetters.getSpotifyUrl(featured),
                FeaturedArtistGetters.getSpotifyId(featured),
                FeaturedArtistCuratedGetters.getCuratorReason(curated),
                FeaturedArtistCuratedGetters.getSpotlightOrder(curated)
        );
    }

    private static String resolveDisplayGenre(String genres) {
        if (genres == null || genres.isBlank()) {
            return null;
        }
        int commaIndex = genres.indexOf(',');
        if (commaIndex < 0) {
            return genres;
        }
        return genres.substring(0, commaIndex);
    }

    public static ArtistHomeResponse fromIndependent(ArtistProfile profile) {
        return new ArtistHomeResponse(
                ArtistProfileGetters.getId(profile),
                ArtistProfileGetters.getStageName(profile),
                ArtistProfileGetters.getAvatarUrl(profile),
                null,
                ArtistProfileGetters.getBio(profile),
                HighlightedArtistType.INDEPENDENT,
                ArtistProfileGetters.getSpotifyUrl(profile),
                ArtistProfileGetters.getSpotifyId(profile),
                null,
                null
        );
    }
}

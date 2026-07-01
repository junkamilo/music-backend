package com.musicplatform.backend_core.artist.featured.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.musicplatform.backend_core.artist.entity.ArtistProfile;
import com.musicplatform.backend_core.artist.entity.ArtistProfileFactory;
import com.musicplatform.backend_core.artist.entity.ArtistProfileSetters;
import com.musicplatform.backend_core.artist.entity.ArtistType;
import com.musicplatform.backend_core.artist.featured.dto.response.ArtistHomeResponse;
import com.musicplatform.backend_core.artist.featured.dto.response.HighlightedArtistType;
import com.musicplatform.backend_core.artist.featured.entity.FeaturedArtist;
import com.musicplatform.backend_core.artist.featured.entity.FeaturedArtistCurated;
import com.musicplatform.backend_core.artist.featured.entity.FeaturedArtistCuratedFactory;
import com.musicplatform.backend_core.artist.featured.entity.FeaturedArtistCuratedSetters;
import com.musicplatform.backend_core.artist.featured.entity.FeaturedArtistFactory;
import com.musicplatform.backend_core.artist.featured.entity.FeaturedArtistSetters;
import com.musicplatform.backend_core.artist.featured.entity.SyncStatus;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class ArtistHomeMapperTest {

    @Test
    void fromCurated_mapsFeaturedArtistFields() {
        FeaturedArtist featured = FeaturedArtistFactory.createEmpty();
        FeaturedArtistSetters.setSpotifyId(featured, "spotify-1");
        FeaturedArtistSetters.setStageName(featured, "Shakira");
        FeaturedArtistSetters.setImageUrl(featured, "https://example.com/shakira.jpg");
        FeaturedArtistSetters.setSpotifyUrl(featured, "https://open.spotify.com/artist/spotify-1");
        FeaturedArtistSetters.setGenres(featured, "reggaeton,latin-pop");
        FeaturedArtistSetters.setSyncStatus(featured, SyncStatus.SYNCED);

        FeaturedArtistCurated curated = FeaturedArtistCuratedFactory.createEmpty();
        FeaturedArtistCuratedSetters.setFeaturedArtist(curated, featured);
        FeaturedArtistCuratedSetters.setCuratorReason(curated, "Trending");
        FeaturedArtistCuratedSetters.setSpotlightOrder(curated, 1);
        FeaturedArtistCuratedSetters.setFeaturedFrom(curated, LocalDate.now());

        ArtistHomeResponse response = ArtistHomeMapper.fromCurated(curated);

        assertEquals("Shakira", response.name());
        assertEquals("https://example.com/shakira.jpg", response.imageUrl());
        assertEquals("reggaeton", response.genre());
        assertNull(response.bio());
        assertEquals(HighlightedArtistType.FEATURED, response.type());
        assertEquals("spotify-1", response.spotifyId());
        assertEquals("Trending", response.curatorReason());
        assertEquals(1, response.spotlightOrder());
    }

    @Test
    void fromFeatured_mapsSyncedSpotifyArtistFields() {
        FeaturedArtist featured = FeaturedArtistFactory.createEmpty();
        FeaturedArtistSetters.setSpotifyId(featured, "spotify-1");
        FeaturedArtistSetters.setStageName(featured, "Shakira");
        FeaturedArtistSetters.setImageUrl(featured, "https://example.com/shakira.jpg");
        FeaturedArtistSetters.setSpotifyUrl(featured, "https://open.spotify.com/artist/spotify-1");
        FeaturedArtistSetters.setGenres(featured, "reggaeton,latin-pop");
        FeaturedArtistSetters.setSyncStatus(featured, SyncStatus.SYNCED);

        ArtistHomeResponse response = ArtistHomeMapper.fromFeatured(featured);

        assertEquals("Shakira", response.name());
        assertEquals("https://example.com/shakira.jpg", response.imageUrl());
        assertEquals("reggaeton", response.genre());
        assertEquals(HighlightedArtistType.FEATURED, response.type());
        assertEquals("spotify-1", response.spotifyId());
        assertNull(response.curatorReason());
        assertNull(response.spotlightOrder());
    }

    @Test
    void fromIndependent_mapsCreatorProfileFields() {
        ArtistProfile profile = ArtistProfileFactory.createEmpty();
        ArtistProfileSetters.setStageName(profile, "Juan Beltrán");
        ArtistProfileSetters.setBio(profile, "Reggaeton emergente");
        ArtistProfileSetters.setAvatarUrl(profile, "https://example.com/juan.jpg");
        ArtistProfileSetters.setArtistType(profile, ArtistType.INDEPENDENT);
        ArtistProfileSetters.setActive(profile, true);

        ArtistHomeResponse response = ArtistHomeMapper.fromIndependent(profile);

        assertEquals("Juan Beltrán", response.name());
        assertEquals("https://example.com/juan.jpg", response.imageUrl());
        assertEquals("Reggaeton emergente", response.bio());
        assertEquals(HighlightedArtistType.INDEPENDENT, response.type());
        assertNull(response.genre());
        assertNull(response.curatorReason());
        assertNull(response.spotlightOrder());
    }
}

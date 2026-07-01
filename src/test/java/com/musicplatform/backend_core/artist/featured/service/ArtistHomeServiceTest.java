package com.musicplatform.backend_core.artist.featured.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.musicplatform.backend_core.artist.entity.ArtistProfile;
import com.musicplatform.backend_core.artist.entity.ArtistProfileFactory;
import com.musicplatform.backend_core.artist.entity.ArtistProfileSetters;
import com.musicplatform.backend_core.artist.entity.ArtistType;
import com.musicplatform.backend_core.artist.featured.dto.response.ArtistHomeResponse;
import com.musicplatform.backend_core.artist.featured.dto.response.HighlightedArtistType;
import com.musicplatform.backend_core.artist.featured.entity.FeaturedArtist;
import com.musicplatform.backend_core.artist.featured.entity.FeaturedArtistFactory;
import com.musicplatform.backend_core.artist.featured.entity.FeaturedArtistSetters;
import com.musicplatform.backend_core.artist.featured.entity.SyncStatus;
import com.musicplatform.backend_core.artist.featured.repository.ArtistInspirationRepository;
import com.musicplatform.backend_core.artist.featured.repository.FeaturedArtistRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class ArtistHomeServiceTest {

    @Mock
    private FeaturedArtistRepository featuredArtistRepository;

    @Mock
    private ArtistInspirationRepository artistInspirationRepository;

    private ArtistHomeService artistHomeService;

    @BeforeEach
    void setUp() {
        artistHomeService = new ArtistHomeService(
                featuredArtistRepository,
                artistInspirationRepository
        );
    }

    @Test
    void getHighlightedArtists_returnsSyncedSpotifyArtistsOrderedByPopularity() {
        FeaturedArtist shakira = FeaturedArtistFactory.createEmpty();
        FeaturedArtistSetters.setSpotifyId(shakira, "spotify-1");
        FeaturedArtistSetters.setStageName(shakira, "Shakira");
        FeaturedArtistSetters.setImageUrl(shakira, "https://example.com/shakira.jpg");
        FeaturedArtistSetters.setGenres(shakira, "reggaeton,latin-pop");
        FeaturedArtistSetters.setSyncStatus(shakira, SyncStatus.SYNCED);
        FeaturedArtistSetters.setPopularity(shakira, 94);

        FeaturedArtist badBunny = FeaturedArtistFactory.createEmpty();
        FeaturedArtistSetters.setSpotifyId(badBunny, "spotify-2");
        FeaturedArtistSetters.setStageName(badBunny, "Bad Bunny");
        FeaturedArtistSetters.setSyncStatus(badBunny, SyncStatus.SYNCED);
        FeaturedArtistSetters.setPopularity(badBunny, 98);

        when(featuredArtistRepository.findBySyncStatusOrderByPopularityDesc(
                eq(SyncStatus.SYNCED),
                any(Pageable.class)
        )).thenReturn(List.of(badBunny, shakira));

        List<ArtistHomeResponse> result = artistHomeService.getHighlightedArtists(3);

        assertEquals(2, result.size());
        assertEquals("Bad Bunny", result.get(0).name());
        assertEquals("reggaeton", result.get(1).genre());
        assertEquals(HighlightedArtistType.FEATURED, result.get(0).type());
        assertEquals("https://example.com/shakira.jpg", result.get(1).imageUrl());
    }

    @Test
    void getInspiredCreators_mapsIndependentProfiles() {
        ArtistProfile independent = ArtistProfileFactory.createEmpty();
        ArtistProfileSetters.setStageName(independent, "María Gómez");
        ArtistProfileSetters.setArtistType(independent, ArtistType.INDEPENDENT);
        ArtistProfileSetters.setActive(independent, true);

        when(artistInspirationRepository.findInspiredCreators(eq(1L), any(Pageable.class)))
                .thenReturn(List.of(independent));

        List<ArtistHomeResponse> result = artistHomeService.getInspiredCreators(1L, 5);

        assertEquals(1, result.size());
        assertEquals("María Gómez", result.get(0).name());
        assertEquals(HighlightedArtistType.INDEPENDENT, result.get(0).type());
    }
}

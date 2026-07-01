package com.musicplatform.backend_core.artist.featured.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.musicplatform.backend_core.artist.featured.dto.response.ArtistHomeResponse;
import com.musicplatform.backend_core.artist.featured.entity.FeaturedArtist;
import com.musicplatform.backend_core.artist.featured.entity.FeaturedArtistFactory;
import com.musicplatform.backend_core.artist.featured.entity.FeaturedArtistSetters;
import com.musicplatform.backend_core.artist.featured.entity.SyncStatus;
import com.musicplatform.backend_core.artist.featured.repository.ArtistInspirationRepository;
import com.musicplatform.backend_core.artist.featured.repository.FeaturedArtistRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class ArtistHomeServiceCacheTest {

    @Autowired
    private ArtistHomeService artistHomeService;

    @MockitoBean
    private FeaturedArtistRepository featuredArtistRepository;

    @MockitoBean
    private ArtistInspirationRepository artistInspirationRepository;

    @Test
    void getHighlightedArtists_usesCacheOnSecondCall() {
        FeaturedArtist featured = FeaturedArtistFactory.createEmpty();
        FeaturedArtistSetters.setSpotifyId(featured, "spotify-cache-test");
        FeaturedArtistSetters.setStageName(featured, "Cached Artist");
        FeaturedArtistSetters.setSyncStatus(featured, SyncStatus.SYNCED);
        FeaturedArtistSetters.setPopularity(featured, 80);

        when(featuredArtistRepository.findBySyncStatusOrderByPopularityDesc(
                eq(SyncStatus.SYNCED),
                any(Pageable.class)
        )).thenReturn(List.of(featured));

        List<ArtistHomeResponse> first = artistHomeService.getHighlightedArtists(6);
        List<ArtistHomeResponse> second = artistHomeService.getHighlightedArtists(6);

        assertEquals(1, first.size());
        assertEquals("Cached Artist", first.get(0).name());
        assertEquals(first.get(0).name(), second.get(0).name());
        verify(featuredArtistRepository, times(1))
                .findBySyncStatusOrderByPopularityDesc(eq(SyncStatus.SYNCED), any(Pageable.class));
    }
}

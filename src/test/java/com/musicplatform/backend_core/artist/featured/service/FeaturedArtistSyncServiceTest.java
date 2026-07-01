package com.musicplatform.backend_core.artist.featured.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.musicplatform.backend_core.artist.featured.entity.FeaturedArtist;
import com.musicplatform.backend_core.artist.featured.entity.FeaturedArtistFactory;
import com.musicplatform.backend_core.artist.featured.entity.FeaturedArtistGetters;
import com.musicplatform.backend_core.artist.featured.entity.FeaturedArtistSetters;
import com.musicplatform.backend_core.artist.featured.entity.SyncStatus;
import com.musicplatform.backend_core.artist.featured.repository.FeaturedArtistRepository;
import com.musicplatform.backend_core.artist.featured.repository.SpotifyTopArtistSeedRepository;
import com.musicplatform.backend_core.spotify.dto.external.SpotifyExternalUrls;
import com.musicplatform.backend_core.spotify.dto.external.SpotifyFullArtistObject;
import com.musicplatform.backend_core.spotify.dto.external.SpotifyImage;
import com.musicplatform.backend_core.spotify.service.SpotifyService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FeaturedArtistSyncServiceTest {

    @Mock
    private FeaturedArtistRepository featuredArtistRepository;

    @Mock
    private SpotifyTopArtistSeedRepository spotifyTopArtistSeedRepository;

    @Mock
    private SpotifyService spotifyService;

    @Mock
    private HighlightedArtistsCacheService highlightedArtistsCacheService;

    private FeaturedArtistSyncService featuredArtistSyncService;

    @BeforeEach
    void setUp() {
        featuredArtistSyncService = new FeaturedArtistSyncService(
                featuredArtistRepository,
                spotifyTopArtistSeedRepository,
                spotifyService,
                highlightedArtistsCacheService
        );
    }

    @Test
    void syncSingleArtist_mapsGenresPopularityAndMarksSynced() {
        FeaturedArtist artist = FeaturedArtistFactory.createEmpty();
        FeaturedArtistSetters.setSpotifyId(artist, "spotify-1");
        FeaturedArtistSetters.setStageName(artist, "Old Name");

        SpotifyFullArtistObject spotifyArtist = buildSpotifyArtist();
        when(spotifyService.getFullArtistById("spotify-1")).thenReturn(spotifyArtist);
        when(featuredArtistRepository.findBySpotifyId("spotify-1")).thenReturn(Optional.of(artist));
        when(featuredArtistRepository.save(any(FeaturedArtist.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        FeaturedArtist synced = featuredArtistSyncService.syncSingleArtist(artist);

        assertEquals("Shakira", FeaturedArtistGetters.getStageName(synced));
        assertEquals(94, FeaturedArtistGetters.getPopularity(synced));
        assertEquals("reggaeton,latin-pop", FeaturedArtistGetters.getGenres(synced));
        assertEquals(SyncStatus.SYNCED, FeaturedArtistGetters.getSyncStatus(synced));
        verify(spotifyService).getFullArtistById("spotify-1");
    }

    @Test
    void upsertFromSpotify_createsArtistWhenMissing() {
        SpotifyFullArtistObject spotifyArtist = buildSpotifyArtist();
        when(spotifyService.getFullArtistById(anyString())).thenReturn(spotifyArtist);
        when(featuredArtistRepository.findBySpotifyId("spotify-1")).thenReturn(Optional.empty());
        when(featuredArtistRepository.save(org.mockito.ArgumentMatchers.any(FeaturedArtist.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        FeaturedArtist saved = featuredArtistSyncService.upsertFromSpotify("spotify-1");

        assertEquals("spotify-1", FeaturedArtistGetters.getSpotifyId(saved));
        assertEquals("https://open.spotify.com/artist/spotify-1", FeaturedArtistGetters.getSpotifyUrl(saved));
    }

    private SpotifyFullArtistObject buildSpotifyArtist() {
        SpotifyFullArtistObject artist = new SpotifyFullArtistObject();
        artist.setId("spotify-1");
        artist.setName("Shakira");
        artist.setPopularity(94);
        artist.setGenres(List.of("reggaeton", "latin-pop"));

        SpotifyImage image = new SpotifyImage();
        image.setUrl("https://example.com/shakira.jpg");
        image.setHeight(640);
        artist.setImages(List.of(image));

        SpotifyExternalUrls externalUrls = new SpotifyExternalUrls();
        externalUrls.setSpotify("https://open.spotify.com/artist/spotify-1");
        artist.setExternalUrls(externalUrls);
        return artist;
    }
}

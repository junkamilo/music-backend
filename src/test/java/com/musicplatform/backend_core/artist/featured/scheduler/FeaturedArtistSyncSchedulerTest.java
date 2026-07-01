package com.musicplatform.backend_core.artist.featured.scheduler;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.musicplatform.backend_core.artist.featured.dto.response.FeaturedArtistSyncResult;
import com.musicplatform.backend_core.artist.featured.service.FeaturedArtistSyncService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class FeaturedArtistSyncSchedulerDisabledTest {

    @Autowired(required = false)
    private FeaturedArtistSyncScheduler featuredArtistSyncScheduler;

    @Test
    void schedulerBeanNotLoadedWhenSyncDisabled() {
        assertNull(featuredArtistSyncScheduler);
    }
}

@SpringBootTest
@TestPropertySource(properties = {
        "app.spotify.sync.enabled=true",
        "app.spotify.sync.cron=0 0 3 * * *"
})
class FeaturedArtistSyncSchedulerEnabledTest {

    @Autowired
    private FeaturedArtistSyncScheduler featuredArtistSyncScheduler;

    @MockitoBean
    private FeaturedArtistSyncService featuredArtistSyncService;

    @Test
    void syncFeaturedArtistsNightly_runsFullSync() {
        when(featuredArtistSyncService.runFullSync()).thenReturn(new FeaturedArtistSyncResult(2, 0));

        featuredArtistSyncScheduler.syncFeaturedArtistsNightly();

        verify(featuredArtistSyncService).runFullSync();
    }
}

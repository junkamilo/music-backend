package com.musicplatform.backend_core.artist.featured.scheduler;

import com.musicplatform.backend_core.artist.featured.dto.response.FeaturedArtistSyncResult;
import com.musicplatform.backend_core.artist.featured.service.FeaturedArtistSyncService;
import com.musicplatform.backend_core.config.SpotifySyncProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.spotify.sync.enabled", havingValue = "true", matchIfMissing = true)
@Slf4j
public class FeaturedArtistSyncScheduler {

    private final FeaturedArtistSyncService featuredArtistSyncService;
    private final SpotifySyncProperties spotifySyncProperties;

    public FeaturedArtistSyncScheduler(
            FeaturedArtistSyncService featuredArtistSyncService,
            SpotifySyncProperties spotifySyncProperties
    ) {
        this.featuredArtistSyncService = featuredArtistSyncService;
        this.spotifySyncProperties = spotifySyncProperties;
    }

    @Scheduled(cron = "${app.spotify.sync.cron}")
    public void syncFeaturedArtistsNightly() {
        if (!spotifySyncProperties.isEnabled()) {
            return;
        }

        log.info("Iniciando sincronización nocturna de artistas destacados desde Spotify");
        FeaturedArtistSyncResult result = featuredArtistSyncService.runFullSync();
        log.info(
                "Sincronización nocturna completada: {} exitosos, {} fallos",
                result.synced(),
                result.failed()
        );
    }
}

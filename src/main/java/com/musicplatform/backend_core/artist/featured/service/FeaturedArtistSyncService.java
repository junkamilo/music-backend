package com.musicplatform.backend_core.artist.featured.service;

import com.musicplatform.backend_core.artist.featured.dto.response.FeaturedArtistSyncResult;
import com.musicplatform.backend_core.artist.featured.entity.FeaturedArtist;
import com.musicplatform.backend_core.artist.featured.entity.FeaturedArtistFactory;
import com.musicplatform.backend_core.artist.featured.entity.FeaturedArtistGetters;
import com.musicplatform.backend_core.artist.featured.entity.FeaturedArtistSetters;
import com.musicplatform.backend_core.artist.featured.entity.SpotifyTopArtistSeed;
import com.musicplatform.backend_core.artist.featured.entity.SpotifyTopArtistSeedGetters;
import com.musicplatform.backend_core.artist.featured.entity.SpotifyTopArtistSeedSetters;
import com.musicplatform.backend_core.artist.featured.entity.SyncStatus;
import com.musicplatform.backend_core.artist.featured.repository.FeaturedArtistRepository;
import com.musicplatform.backend_core.artist.featured.repository.SpotifyTopArtistSeedRepository;
import com.musicplatform.backend_core.spotify.dto.external.SpotifyExternalUrls;
import com.musicplatform.backend_core.spotify.dto.external.SpotifyFullArtistObject;
import com.musicplatform.backend_core.spotify.dto.external.SpotifyImage;
import com.musicplatform.backend_core.spotify.dto.response.SpotifyArtistResponse;
import com.musicplatform.backend_core.spotify.service.SpotifyService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class FeaturedArtistSyncService {

    private final FeaturedArtistRepository featuredArtistRepository;
    private final SpotifyTopArtistSeedRepository spotifyTopArtistSeedRepository;
    private final SpotifyService spotifyService;
    private final HighlightedArtistsCacheService highlightedArtistsCacheService;

    public FeaturedArtistSyncService(
            FeaturedArtistRepository featuredArtistRepository,
            SpotifyTopArtistSeedRepository spotifyTopArtistSeedRepository,
            SpotifyService spotifyService,
            HighlightedArtistsCacheService highlightedArtistsCacheService
    ) {
        this.featuredArtistRepository = featuredArtistRepository;
        this.spotifyTopArtistSeedRepository = spotifyTopArtistSeedRepository;
        this.spotifyService = spotifyService;
        this.highlightedArtistsCacheService = highlightedArtistsCacheService;
    }

    @Transactional
    public FeaturedArtist upsertFromSpotify(String spotifyId) {
        SpotifyFullArtistObject spotifyArtist = spotifyService.getFullArtistById(spotifyId);
        return applySpotifyData(spotifyArtist);
    }

    @Transactional
    public FeaturedArtist findOrCreateBySpotifySearch(String query) {
        List<SpotifyArtistResponse> results = spotifyService.searchArtists(query, 1);
        if (results.isEmpty()) {
            throw new IllegalArgumentException("No se encontraron artistas en Spotify para: " + query);
        }
        return upsertFromSpotify(results.get(0).id());
    }

    @Transactional
    public FeaturedArtist syncSingleArtist(FeaturedArtist artist) {
        SpotifyFullArtistObject spotifyArtist = spotifyService.getFullArtistById(
                FeaturedArtistGetters.getSpotifyId(artist)
        );
        return applySpotifyData(spotifyArtist);
    }

    @Transactional
    public FeaturedArtistSyncResult syncAllFeaturedArtists() {
        List<FeaturedArtist> artists = featuredArtistRepository.findAll();
        int synced = 0;
        int failed = 0;

        for (FeaturedArtist artist : artists) {
            try {
                syncSingleArtist(artist);
                synced++;
            } catch (Exception e) {
                log.warn(
                        "Error sincronizando featured artist id={} spotifyId={}",
                        FeaturedArtistGetters.getId(artist),
                        FeaturedArtistGetters.getSpotifyId(artist),
                        e
                );
                FeaturedArtistSetters.setSyncStatus(artist, SyncStatus.FAILED);
                featuredArtistRepository.save(artist);
                failed++;
            }
        }

        return new FeaturedArtistSyncResult(synced, failed);
    }

    @Transactional
    public FeaturedArtistSyncResult syncFromSeedCatalog() {
        List<SpotifyTopArtistSeed> seeds = spotifyTopArtistSeedRepository.findAllByOrderBySyncPriorityAscStageNameAsc();
        int synced = 0;
        int failed = 0;

        for (SpotifyTopArtistSeed seed : seeds) {
            try {
                upsertFromSpotify(SpotifyTopArtistSeedGetters.getSpotifyId(seed));
                SpotifyTopArtistSeedSetters.setLastSyncedAt(seed, LocalDateTime.now());
                spotifyTopArtistSeedRepository.save(seed);
                synced++;
            } catch (Exception e) {
                log.warn(
                        "Error sincronizando seed spotifyId={}",
                        SpotifyTopArtistSeedGetters.getSpotifyId(seed),
                        e
                );
                failed++;
            }
        }

        return new FeaturedArtistSyncResult(synced, failed);
    }

    @Transactional
    public int syncPending() {
        List<FeaturedArtist> pending = featuredArtistRepository.findBySyncStatusIn(
                List.of(SyncStatus.PENDING, SyncStatus.FAILED)
        );
        int synced = 0;
        for (FeaturedArtist artist : pending) {
            try {
                syncSingleArtist(artist);
                synced++;
            } catch (Exception e) {
                log.warn(
                        "No se pudo sincronizar featured artist id={} spotifyId={}",
                        FeaturedArtistGetters.getId(artist),
                        FeaturedArtistGetters.getSpotifyId(artist),
                        e
                );
                FeaturedArtistSetters.setSyncStatus(artist, SyncStatus.FAILED);
                featuredArtistRepository.save(artist);
            }
        }
        return synced;
    }

    @Transactional
    public FeaturedArtistSyncResult runFullSync() {
        FeaturedArtistSyncResult result = syncFromSeedCatalog();
        highlightedArtistsCacheService.evictHighlightedArtists();
        return result;
    }

    private FeaturedArtist applySpotifyData(SpotifyFullArtistObject spotifyArtist) {
        FeaturedArtist artist = featuredArtistRepository.findBySpotifyId(spotifyArtist.getId())
                .orElseGet(FeaturedArtistFactory::createEmpty);

        FeaturedArtistSetters.setSpotifyId(artist, spotifyArtist.getId());
        FeaturedArtistSetters.setStageName(artist, spotifyArtist.getName());
        FeaturedArtistSetters.setImageUrl(artist, resolveImageUrl(spotifyArtist.getImages()));
        FeaturedArtistSetters.setSpotifyUrl(artist, resolveSpotifyUrl(spotifyArtist.getExternalUrls()));
        FeaturedArtistSetters.setPopularity(artist, spotifyArtist.getPopularity());
        FeaturedArtistSetters.setGenres(artist, resolveGenres(spotifyArtist.getGenres()));
        FeaturedArtistSetters.setSyncStatus(artist, SyncStatus.SYNCED);
        FeaturedArtistSetters.setLastSyncedAt(artist, LocalDateTime.now());

        return featuredArtistRepository.save(artist);
    }

    private String resolveSpotifyUrl(SpotifyExternalUrls externalUrls) {
        return externalUrls != null ? externalUrls.getSpotify() : null;
    }

    private String resolveGenres(List<String> genres) {
        if (genres == null || genres.isEmpty()) {
            return null;
        }
        return String.join(",", genres);
    }

    private String resolveImageUrl(List<SpotifyImage> images) {
        if (images == null || images.isEmpty()) {
            return null;
        }
        return images.stream()
                .filter(image -> image.getUrl() != null)
                .max((left, right) -> {
                    int leftHeight = left.getHeight() != null ? left.getHeight() : 0;
                    int rightHeight = right.getHeight() != null ? right.getHeight() : 0;
                    return Integer.compare(leftHeight, rightHeight);
                })
                .map(SpotifyImage::getUrl)
                .orElse(images.get(0).getUrl());
    }
}

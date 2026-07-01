package com.musicplatform.backend_core.artist.featured.service;

import com.musicplatform.backend_core.artist.featured.dto.response.ArtistHomeResponse;
import com.musicplatform.backend_core.artist.featured.entity.SyncStatus;
import com.musicplatform.backend_core.artist.featured.mapper.ArtistHomeMapper;
import com.musicplatform.backend_core.artist.featured.repository.ArtistInspirationRepository;
import com.musicplatform.backend_core.artist.featured.repository.FeaturedArtistRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ArtistHomeService {

    private static final int MIN_LIMIT = 1;
    private static final int MAX_LIMIT = 20;
    private static final int DEFAULT_LIMIT = 6;

    private final FeaturedArtistRepository featuredArtistRepository;
    private final ArtistInspirationRepository artistInspirationRepository;

    public ArtistHomeService(
            FeaturedArtistRepository featuredArtistRepository,
            ArtistInspirationRepository artistInspirationRepository
    ) {
        this.featuredArtistRepository = featuredArtistRepository;
        this.artistInspirationRepository = artistInspirationRepository;
    }

    @Cacheable(value = "highlightedArtists", key = "'limit_' + #limit")
    public List<ArtistHomeResponse> getHighlightedArtists(int limit) {
        int resolvedLimit = resolveLimit(limit);
        return featuredArtistRepository
                .findBySyncStatusOrderByPopularityDesc(SyncStatus.SYNCED, PageRequest.of(0, resolvedLimit))
                .stream()
                .map(ArtistHomeMapper::fromFeatured)
                .toList();
    }

    public List<ArtistHomeResponse> getInspiredCreators(Long featuredArtistId, int limit) {
        int resolvedLimit = resolveLimit(limit);
        try {
            return artistInspirationRepository
                    .findInspiredCreators(featuredArtistId, PageRequest.of(0, resolvedLimit))
                    .stream()
                    .map(ArtistHomeMapper::fromIndependent)
                    .toList();
        } catch (Exception e) {
            log.warn("Error obteniendo creadores inspirados para featuredArtistId={}", featuredArtistId, e);
            return List.of();
        }
    }

    private int resolveLimit(int limit) {
        if (limit < MIN_LIMIT) {
            return DEFAULT_LIMIT;
        }
        return Math.min(limit, MAX_LIMIT);
    }
}

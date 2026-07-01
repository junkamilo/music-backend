package com.musicplatform.backend_core.artist.featured.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class HighlightedArtistsCacheService {

    @CacheEvict(value = "highlightedArtists", allEntries = true)
    public void evictHighlightedArtists() {
        // Invalidación declarativa del caché del home.
    }
}

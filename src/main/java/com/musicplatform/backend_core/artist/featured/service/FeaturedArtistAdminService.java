package com.musicplatform.backend_core.artist.featured.service;

import com.musicplatform.backend_core.artist.exception.ForbiddenProfileAccessException;
import com.musicplatform.backend_core.artist.featured.dto.response.FeaturedArtistAdminResponse;
import com.musicplatform.backend_core.artist.featured.dto.response.FeaturedArtistSyncResult;
import com.musicplatform.backend_core.artist.featured.entity.FeaturedArtist;
import com.musicplatform.backend_core.artist.featured.exception.FeaturedArtistAlreadyExistsException;
import com.musicplatform.backend_core.artist.featured.exception.FeaturedArtistNotFoundException;
import com.musicplatform.backend_core.artist.featured.mapper.FeaturedArtistAdminMapper;
import com.musicplatform.backend_core.artist.featured.repository.FeaturedArtistRepository;
import com.musicplatform.backend_core.auth.domain.UserDomain;
import com.musicplatform.backend_core.auth.entity.User;
import com.musicplatform.backend_core.auth.exception.AccountInactiveException;
import com.musicplatform.backend_core.auth.exception.InvalidCredentialsException;
import com.musicplatform.backend_core.auth.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeaturedArtistAdminService {

    private final UserRepository userRepository;
    private final FeaturedArtistRepository featuredArtistRepository;
    private final FeaturedArtistSyncService featuredArtistSyncService;
    private final HighlightedArtistsCacheService highlightedArtistsCacheService;

    public FeaturedArtistAdminService(
            UserRepository userRepository,
            FeaturedArtistRepository featuredArtistRepository,
            FeaturedArtistSyncService featuredArtistSyncService,
            HighlightedArtistsCacheService highlightedArtistsCacheService
    ) {
        this.userRepository = userRepository;
        this.featuredArtistRepository = featuredArtistRepository;
        this.featuredArtistSyncService = featuredArtistSyncService;
        this.highlightedArtistsCacheService = highlightedArtistsCacheService;
    }

    @Transactional
    public FeaturedArtistAdminResponse syncArtistNow(Long featuredArtistId, Long adminUserId) {
        requireAdmin(adminUserId);
        FeaturedArtist artist = featuredArtistRepository.findById(featuredArtistId)
                .orElseThrow(FeaturedArtistNotFoundException::new);
        FeaturedArtist synced = featuredArtistSyncService.syncSingleArtist(artist);
        highlightedArtistsCacheService.evictHighlightedArtists();
        return FeaturedArtistAdminMapper.toAdminResponse(synced);
    }

    @Transactional
    public FeaturedArtistAdminResponse addArtistFromSpotify(String spotifyArtistId, Long adminUserId) {
        requireAdmin(adminUserId);
        if (featuredArtistRepository.existsBySpotifyId(spotifyArtistId)) {
            throw new FeaturedArtistAlreadyExistsException();
        }
        FeaturedArtist saved = featuredArtistSyncService.upsertFromSpotify(spotifyArtistId);
        highlightedArtistsCacheService.evictHighlightedArtists();
        return FeaturedArtistAdminMapper.toAdminResponse(saved);
    }

    @Transactional
    public FeaturedArtistSyncResult syncAllNow(Long adminUserId) {
        requireAdmin(adminUserId);
        return featuredArtistSyncService.runFullSync();
    }

    private void requireAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(InvalidCredentialsException::new);
        if (!UserDomain.canAccessPlatform(user)) {
            throw new AccountInactiveException();
        }
        if (!UserDomain.isAdmin(user)) {
            throw new ForbiddenProfileAccessException("Solo los administradores pueden realizar esta acción");
        }
    }
}

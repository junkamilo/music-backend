package com.musicplatform.backend_core.artist.service;

import com.musicplatform.backend_core.artist.domain.ArtistProfileDomain;
import com.musicplatform.backend_core.artist.dto.request.CreateArtistProfileRequest;
import com.musicplatform.backend_core.artist.dto.request.UpdateArtistProfileRequest;
import com.musicplatform.backend_core.artist.dto.response.ArtistProfileResponse;
import com.musicplatform.backend_core.artist.dto.response.ArtistPublicProfileResponse;
import com.musicplatform.backend_core.artist.dto.response.FeaturedArtistSummary;
import com.musicplatform.backend_core.artist.entity.ArtistProfile;
import com.musicplatform.backend_core.artist.entity.ArtistProfileFactory;
import com.musicplatform.backend_core.artist.entity.ArtistProfileGetters;
import com.musicplatform.backend_core.artist.entity.ArtistProfileSetters;
import com.musicplatform.backend_core.artist.entity.ArtistType;
import com.musicplatform.backend_core.artist.exception.ArtistProfileAlreadyExistsException;
import com.musicplatform.backend_core.artist.exception.ArtistProfileNotFoundException;
import com.musicplatform.backend_core.artist.exception.ForbiddenProfileAccessException;
import com.musicplatform.backend_core.artist.exception.ProfileNotActiveException;
import com.musicplatform.backend_core.artist.mapper.ArtistProfileMapper;
import com.musicplatform.backend_core.artist.milestone.service.ArtistMilestoneService;
import com.musicplatform.backend_core.artist.repository.ArtistProfileRepository;
import com.musicplatform.backend_core.artist.timeline.service.ArtistTimelineEventService;
import com.musicplatform.backend_core.auth.domain.UserDomain;
import com.musicplatform.backend_core.auth.entity.User;
import com.musicplatform.backend_core.auth.entity.UserGetters;
import com.musicplatform.backend_core.auth.entity.UserSetters;
import com.musicplatform.backend_core.auth.exception.AccountInactiveException;
import com.musicplatform.backend_core.auth.exception.InvalidCredentialsException;
import com.musicplatform.backend_core.auth.repository.UserRepository;
import com.musicplatform.backend_core.shared.enums.UserRole;
import com.musicplatform.backend_core.user.exception.NotCreatorException;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArtistProfileService {

    private final ArtistProfileRepository artistProfileRepository;
    private final UserRepository userRepository;
    private final ArtistMilestoneService artistMilestoneService;
    private final ArtistTimelineEventService artistTimelineEventService;

    public ArtistProfileService(
            ArtistProfileRepository artistProfileRepository,
            UserRepository userRepository,
            ArtistMilestoneService artistMilestoneService,
            ArtistTimelineEventService artistTimelineEventService
    ) {
        this.artistProfileRepository = artistProfileRepository;
        this.userRepository = userRepository;
        this.artistMilestoneService = artistMilestoneService;
        this.artistTimelineEventService = artistTimelineEventService;
    }

    @Transactional
    public ArtistProfileResponse create(Long userId, CreateArtistProfileRequest request) {
        User user = requireActiveUser(userId);
        requireCreator(user);

        if (artistProfileRepository.existsByUserId(userId)) {
            throw new ArtistProfileAlreadyExistsException();
        }

        ArtistProfile profile = ArtistProfileFactory.createEmpty();
        ArtistProfileSetters.setUserId(profile, userId);
        ArtistProfileSetters.setArtistType(profile, ArtistType.INDEPENDENT);
        applyCreate(profile, request);
        ArtistProfileSetters.setVerified(profile, false);
        ArtistProfileSetters.setActive(profile, true);

        ArtistProfile saved = artistProfileRepository.save(profile);
        return ArtistProfileMapper.toResponse(saved);
    }

    @Transactional
    public Optional<ArtistProfileResponse> claimFeaturedProfileOnRegister(
            Long userId,
            String stageName,
            String spotifyId
    ) {
        Optional<ArtistProfile> featured = findUnclaimedFeaturedProfile(stageName, spotifyId);
        if (featured.isEmpty()) {
            return Optional.empty();
        }

        ArtistProfile profile = featured.get();
        if (!ArtistProfileDomain.isUnclaimedFeatured(profile)) {
            return Optional.empty();
        }

        ArtistProfileDomain.claimAsIndependent(profile, userId);
        ArtistProfile saved = artistProfileRepository.save(profile);
        promoteToCreatorIfNeeded(userId);
        return Optional.of(ArtistProfileMapper.toResponse(saved));
    }

    public List<FeaturedArtistSummary> listFeaturedProfiles() {
        return artistProfileRepository.findByArtistTypeAndActiveTrueOrderByStageNameAsc(ArtistType.FEATURED)
                .stream()
                .map(ArtistProfileMapper::toFeaturedSummary)
                .toList();
    }

    public ArtistProfileResponse getMyProfile(Long userId) {
        User user = requireActiveUser(userId);
        requireCreator(user);

        ArtistProfile profile = artistProfileRepository.findByUserId(userId)
                .orElseThrow(ArtistProfileNotFoundException::new);

        return ArtistProfileMapper.toResponse(profile);
    }

    @Transactional
    public ArtistProfileResponse updateMyProfile(Long userId, UpdateArtistProfileRequest request) {
        User user = requireActiveUser(userId);
        requireCreator(user);

        ArtistProfile profile = artistProfileRepository.findByUserId(userId)
                .orElseThrow(ArtistProfileNotFoundException::new);

        if (!ArtistProfileDomain.canBeEditedBy(profile, userId)) {
            throw new ForbiddenProfileAccessException();
        }

        if (!ArtistProfileDomain.isActive(profile)) {
            throw new ProfileNotActiveException();
        }

        applyUpdate(profile, request);

        ArtistProfile saved = artistProfileRepository.save(profile);
        return ArtistProfileMapper.toResponse(saved);
    }

    @Transactional
    public void deactivateMyProfile(Long userId) {
        User user = requireActiveUser(userId);
        requireCreator(user);

        ArtistProfile profile = artistProfileRepository.findByUserId(userId)
                .orElseThrow(ArtistProfileNotFoundException::new);

        if (!ArtistProfileDomain.canBeEditedBy(profile, userId)) {
            throw new ForbiddenProfileAccessException();
        }

        ArtistProfileDomain.deactivate(profile);
        artistProfileRepository.save(profile);
    }

    public ArtistPublicProfileResponse getPublicProfileEnriched(Long id) {
        ArtistProfile profile = artistProfileRepository.findById(id)
                .orElseThrow(ArtistProfileNotFoundException::new);

        if (!ArtistProfileDomain.canBeDisplayedPublicly(profile)) {
            throw new ProfileNotActiveException();
        }

        return ArtistProfileMapper.toPublicResponse(
                profile,
                artistMilestoneService.listByArtistId(id),
                artistTimelineEventService.listByArtistId(id)
        );
    }

    public ArtistProfile requireOwnedProfile(Long userId) {
        User user = requireActiveUser(userId);
        requireCreator(user);

        return artistProfileRepository.findByUserId(userId)
                .orElseThrow(ArtistProfileNotFoundException::new);
    }

    @Transactional
    public ArtistProfileResponse verifyProfile(Long profileId, Long adminUserId) {
        User admin = requireActiveUser(adminUserId);
        requireAdmin(admin);

        ArtistProfile profile = artistProfileRepository.findById(profileId)
                .orElseThrow(ArtistProfileNotFoundException::new);

        ArtistProfileDomain.markVerified(profile);
        ArtistProfile saved = artistProfileRepository.save(profile);
        return ArtistProfileMapper.toResponse(saved);
    }

    @Transactional
    public ArtistProfileResponse revokeVerification(Long profileId, Long adminUserId) {
        User admin = requireActiveUser(adminUserId);
        requireAdmin(admin);

        ArtistProfile profile = artistProfileRepository.findById(profileId)
                .orElseThrow(ArtistProfileNotFoundException::new);

        ArtistProfileDomain.revokeVerification(profile);
        ArtistProfile saved = artistProfileRepository.save(profile);
        return ArtistProfileMapper.toResponse(saved);
    }

    private Optional<ArtistProfile> findUnclaimedFeaturedProfile(String stageName, String spotifyId) {
        String normalizedSpotifyId = normalizeOptional(spotifyId);
        if (normalizedSpotifyId != null) {
            Optional<ArtistProfile> bySpotifyId = artistProfileRepository
                    .findBySpotifyIdAndArtistTypeAndUserIdIsNull(normalizedSpotifyId, ArtistType.FEATURED);
            if (bySpotifyId.isPresent()) {
                return bySpotifyId;
            }
        }

        String normalizedStageName = normalizeOptional(stageName);
        if (normalizedStageName == null) {
            return Optional.empty();
        }

        return artistProfileRepository.findByStageNameIgnoreCaseAndArtistTypeAndUserIdIsNull(
                normalizedStageName,
                ArtistType.FEATURED
        );
    }

    private void promoteToCreatorIfNeeded(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(InvalidCredentialsException::new);
        UserRole role = UserGetters.getRole(user);
        if (role == UserRole.CREATOR || role == UserRole.ADMIN) {
            return;
        }
        UserSetters.setRole(user, UserRole.CREATOR);
        userRepository.save(user);
    }

    private User requireActiveUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(InvalidCredentialsException::new);

        if (!UserDomain.canAccessPlatform(user)) {
            throw new AccountInactiveException();
        }

        return user;
    }

    private void requireCreator(User user) {
        if (!UserDomain.isCreator(user)) {
            throw new NotCreatorException();
        }
    }

    private void requireAdmin(User user) {
        if (!UserDomain.isAdmin(user)) {
            throw new ForbiddenProfileAccessException("Solo los administradores pueden realizar esta acción");
        }
    }

    private void applyCreate(ArtistProfile profile, CreateArtistProfileRequest request) {
        ArtistProfileSetters.setStageName(profile, request.stageName().trim());
        ArtistProfileSetters.setStageNameOrigin(profile, normalizeOptional(request.stageNameOrigin()));
        ArtistProfileSetters.setBio(profile, normalizeOptional(request.bio()));
        ArtistProfileSetters.setAvatarUrl(profile, normalizeOptional(request.avatarUrl()));
        ArtistProfileSetters.setBannerUrl(profile, normalizeOptional(request.bannerUrl()));
        ArtistProfileSetters.setBirthDate(profile, request.birthDate());
        ArtistProfileSetters.setBirthCity(profile, normalizeOptional(request.birthCity()));
        ArtistProfileSetters.setBirthCountry(profile, normalizeOptional(request.birthCountry()));
        ArtistProfileSetters.setCurrentCity(profile, normalizeOptional(request.currentCity()));
        ArtistProfileSetters.setCurrentCountry(profile, normalizeOptional(request.currentCountry()));
        ArtistProfileSetters.setWebsiteUrl(profile, normalizeOptional(request.websiteUrl()));
        ArtistProfileSetters.setInstagramUrl(profile, normalizeOptional(request.instagramUrl()));
        ArtistProfileSetters.setSpotifyUrl(profile, normalizeOptional(request.spotifyUrl()));
    }

    private void applyUpdate(ArtistProfile profile, UpdateArtistProfileRequest request) {
        if (request.stageName() != null) {
            ArtistProfileSetters.setStageName(profile, request.stageName().trim());
        }
        if (request.stageNameOrigin() != null) {
            ArtistProfileSetters.setStageNameOrigin(profile, normalizeOptional(request.stageNameOrigin()));
        }
        if (request.bio() != null) {
            ArtistProfileSetters.setBio(profile, normalizeOptional(request.bio()));
        }
        if (request.avatarUrl() != null) {
            ArtistProfileSetters.setAvatarUrl(profile, normalizeOptional(request.avatarUrl()));
        }
        if (request.bannerUrl() != null) {
            ArtistProfileSetters.setBannerUrl(profile, normalizeOptional(request.bannerUrl()));
        }
        if (request.birthDate() != null) {
            ArtistProfileSetters.setBirthDate(profile, request.birthDate());
        }
        if (request.birthCity() != null) {
            ArtistProfileSetters.setBirthCity(profile, normalizeOptional(request.birthCity()));
        }
        if (request.birthCountry() != null) {
            ArtistProfileSetters.setBirthCountry(profile, normalizeOptional(request.birthCountry()));
        }
        if (request.currentCity() != null) {
            ArtistProfileSetters.setCurrentCity(profile, normalizeOptional(request.currentCity()));
        }
        if (request.currentCountry() != null) {
            ArtistProfileSetters.setCurrentCountry(profile, normalizeOptional(request.currentCountry()));
        }
        if (request.websiteUrl() != null) {
            ArtistProfileSetters.setWebsiteUrl(profile, normalizeOptional(request.websiteUrl()));
        }
        if (request.instagramUrl() != null) {
            ArtistProfileSetters.setInstagramUrl(profile, normalizeOptional(request.instagramUrl()));
        }
        if (request.spotifyUrl() != null) {
            ArtistProfileSetters.setSpotifyUrl(profile, normalizeOptional(request.spotifyUrl()));
        }
    }

    private String normalizeOptional(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}

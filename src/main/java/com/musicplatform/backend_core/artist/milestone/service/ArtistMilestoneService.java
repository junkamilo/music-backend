package com.musicplatform.backend_core.artist.milestone.service;

import com.musicplatform.backend_core.artist.entity.ArtistProfile;
import com.musicplatform.backend_core.artist.entity.ArtistProfileGetters;
import com.musicplatform.backend_core.artist.exception.ForbiddenProfileAccessException;
import com.musicplatform.backend_core.artist.milestone.domain.ArtistMilestoneDomain;
import com.musicplatform.backend_core.artist.milestone.dto.request.CreateArtistMilestoneRequest;
import com.musicplatform.backend_core.artist.milestone.dto.request.UpdateArtistMilestoneRequest;
import com.musicplatform.backend_core.artist.milestone.dto.response.ArtistMilestoneResponse;
import com.musicplatform.backend_core.artist.milestone.entity.ArtistMilestone;
import com.musicplatform.backend_core.artist.milestone.entity.ArtistMilestoneFactory;
import com.musicplatform.backend_core.artist.milestone.entity.ArtistMilestoneGetters;
import com.musicplatform.backend_core.artist.milestone.entity.ArtistMilestoneSetters;
import com.musicplatform.backend_core.artist.milestone.exception.ArtistMilestoneNotFoundException;
import com.musicplatform.backend_core.artist.milestone.mapper.ArtistMilestoneMapper;
import com.musicplatform.backend_core.artist.milestone.repository.ArtistMilestoneRepository;
import com.musicplatform.backend_core.artist.service.ArtistProfileService;
import java.util.List;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArtistMilestoneService {

    private final ArtistMilestoneRepository artistMilestoneRepository;
    private final ArtistProfileService artistProfileService;

    public ArtistMilestoneService(
            ArtistMilestoneRepository artistMilestoneRepository,
            @Lazy ArtistProfileService artistProfileService
    ) {
        this.artistMilestoneRepository = artistMilestoneRepository;
        this.artistProfileService = artistProfileService;
    }

    public List<ArtistMilestoneResponse> listByArtistId(Long artistId) {
        return artistMilestoneRepository.findByArtistIdOrderByDateAchievedAscOrderPositionAsc(artistId)
                .stream()
                .map(ArtistMilestoneMapper::toResponse)
                .toList();
    }

    public List<ArtistMilestoneResponse> listMyMilestones(Long userId) {
        ArtistProfile profile = artistProfileService.requireOwnedProfile(userId);
        return listByArtistId(ArtistProfileGetters.getId(profile));
    }

    @Transactional
    public ArtistMilestoneResponse create(Long userId, CreateArtistMilestoneRequest request) {
        ArtistProfile profile = artistProfileService.requireOwnedProfile(userId);

        ArtistMilestone milestone = ArtistMilestoneFactory.createEmpty();
        ArtistMilestoneSetters.setArtistId(milestone, ArtistProfileGetters.getId(profile));
        ArtistMilestoneSetters.setTitle(milestone, request.title().trim());
        ArtistMilestoneSetters.setDescription(milestone, normalizeOptional(request.description()));
        ArtistMilestoneSetters.setMilestoneType(milestone, request.milestoneType());
        ArtistMilestoneSetters.setDateAchieved(milestone, request.dateAchieved());
        ArtistMilestoneSetters.setImageUrl(milestone, normalizeOptional(request.imageUrl()));
        ArtistMilestoneSetters.setOrderPosition(milestone, request.orderPosition());

        ArtistMilestone saved = artistMilestoneRepository.save(milestone);
        return ArtistMilestoneMapper.toResponse(saved);
    }

    @Transactional
    public ArtistMilestoneResponse update(Long userId, Long milestoneId, UpdateArtistMilestoneRequest request) {
        ArtistProfile profile = artistProfileService.requireOwnedProfile(userId);
        ArtistMilestone milestone = requireOwnedMilestone(milestoneId, ArtistProfileGetters.getId(profile));

        if (request.title() != null) {
            ArtistMilestoneSetters.setTitle(milestone, request.title().trim());
        }
        if (request.description() != null) {
            ArtistMilestoneSetters.setDescription(milestone, normalizeOptional(request.description()));
        }
        if (request.milestoneType() != null) {
            ArtistMilestoneSetters.setMilestoneType(milestone, request.milestoneType());
        }
        if (request.dateAchieved() != null) {
            ArtistMilestoneSetters.setDateAchieved(milestone, request.dateAchieved());
        }
        if (request.imageUrl() != null) {
            ArtistMilestoneSetters.setImageUrl(milestone, normalizeOptional(request.imageUrl()));
        }
        if (request.orderPosition() != null) {
            ArtistMilestoneSetters.setOrderPosition(milestone, request.orderPosition());
        }

        ArtistMilestone saved = artistMilestoneRepository.save(milestone);
        return ArtistMilestoneMapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long userId, Long milestoneId) {
        ArtistProfile profile = artistProfileService.requireOwnedProfile(userId);
        ArtistMilestone milestone = requireOwnedMilestone(milestoneId, ArtistProfileGetters.getId(profile));
        artistMilestoneRepository.delete(milestone);
    }

    private ArtistMilestone requireOwnedMilestone(Long milestoneId, Long artistId) {
        ArtistMilestone milestone = artistMilestoneRepository.findById(milestoneId)
                .orElseThrow(ArtistMilestoneNotFoundException::new);

        if (!ArtistMilestoneDomain.belongsToArtist(milestone, artistId)) {
            throw new ForbiddenProfileAccessException();
        }

        return milestone;
    }

    private String normalizeOptional(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}

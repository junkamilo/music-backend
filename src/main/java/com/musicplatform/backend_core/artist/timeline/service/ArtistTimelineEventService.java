package com.musicplatform.backend_core.artist.timeline.service;

import com.musicplatform.backend_core.artist.entity.ArtistProfile;
import com.musicplatform.backend_core.artist.entity.ArtistProfileGetters;
import com.musicplatform.backend_core.artist.exception.ForbiddenProfileAccessException;
import com.musicplatform.backend_core.artist.service.ArtistProfileService;
import com.musicplatform.backend_core.artist.timeline.domain.ArtistTimelineEventDomain;
import com.musicplatform.backend_core.artist.timeline.dto.request.CreateArtistTimelineEventRequest;
import com.musicplatform.backend_core.artist.timeline.dto.request.UpdateArtistTimelineEventRequest;
import com.musicplatform.backend_core.artist.timeline.dto.response.ArtistTimelineEventResponse;
import com.musicplatform.backend_core.artist.timeline.entity.ArtistTimelineEvent;
import com.musicplatform.backend_core.artist.timeline.entity.ArtistTimelineEventFactory;
import com.musicplatform.backend_core.artist.timeline.entity.ArtistTimelineEventSetters;
import com.musicplatform.backend_core.artist.timeline.exception.ArtistTimelineEventNotFoundException;
import com.musicplatform.backend_core.artist.timeline.mapper.ArtistTimelineEventMapper;
import com.musicplatform.backend_core.artist.timeline.repository.ArtistTimelineEventRepository;
import java.util.List;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArtistTimelineEventService {

    private final ArtistTimelineEventRepository artistTimelineEventRepository;
    private final ArtistProfileService artistProfileService;

    public ArtistTimelineEventService(
            ArtistTimelineEventRepository artistTimelineEventRepository,
            @Lazy ArtistProfileService artistProfileService
    ) {
        this.artistTimelineEventRepository = artistTimelineEventRepository;
        this.artistProfileService = artistProfileService;
    }

    public List<ArtistTimelineEventResponse> listByArtistId(Long artistId) {
        return artistTimelineEventRepository.findByArtistIdOrderByEventDateAscOrderPositionAsc(artistId)
                .stream()
                .map(ArtistTimelineEventMapper::toResponse)
                .toList();
    }

    public List<ArtistTimelineEventResponse> listMyTimelineEvents(Long userId) {
        ArtistProfile profile = artistProfileService.requireOwnedProfile(userId);
        return listByArtistId(ArtistProfileGetters.getId(profile));
    }

    @Transactional
    public ArtistTimelineEventResponse create(Long userId, CreateArtistTimelineEventRequest request) {
        ArtistProfile profile = artistProfileService.requireOwnedProfile(userId);

        ArtistTimelineEvent event = ArtistTimelineEventFactory.createEmpty();
        ArtistTimelineEventSetters.setArtistId(event, ArtistProfileGetters.getId(profile));
        ArtistTimelineEventSetters.setEventTitle(event, request.eventTitle().trim());
        ArtistTimelineEventSetters.setEventDescription(event, normalizeOptional(request.eventDescription()));
        ArtistTimelineEventSetters.setEventType(event, request.eventType());
        ArtistTimelineEventSetters.setEventDate(event, request.eventDate());
        ArtistTimelineEventSetters.setEventImageUrl(event, normalizeOptional(request.eventImageUrl()));
        ArtistTimelineEventSetters.setOrderPosition(event, request.orderPosition());

        ArtistTimelineEvent saved = artistTimelineEventRepository.save(event);
        return ArtistTimelineEventMapper.toResponse(saved);
    }

    @Transactional
    public ArtistTimelineEventResponse update(Long userId, Long eventId, UpdateArtistTimelineEventRequest request) {
        ArtistProfile profile = artistProfileService.requireOwnedProfile(userId);
        ArtistTimelineEvent event = requireOwnedEvent(eventId, ArtistProfileGetters.getId(profile));

        if (request.eventTitle() != null) {
            ArtistTimelineEventSetters.setEventTitle(event, request.eventTitle().trim());
        }
        if (request.eventDescription() != null) {
            ArtistTimelineEventSetters.setEventDescription(event, normalizeOptional(request.eventDescription()));
        }
        if (request.eventType() != null) {
            ArtistTimelineEventSetters.setEventType(event, request.eventType());
        }
        if (request.eventDate() != null) {
            ArtistTimelineEventSetters.setEventDate(event, request.eventDate());
        }
        if (request.eventImageUrl() != null) {
            ArtistTimelineEventSetters.setEventImageUrl(event, normalizeOptional(request.eventImageUrl()));
        }
        if (request.orderPosition() != null) {
            ArtistTimelineEventSetters.setOrderPosition(event, request.orderPosition());
        }

        ArtistTimelineEvent saved = artistTimelineEventRepository.save(event);
        return ArtistTimelineEventMapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long userId, Long eventId) {
        ArtistProfile profile = artistProfileService.requireOwnedProfile(userId);
        ArtistTimelineEvent event = requireOwnedEvent(eventId, ArtistProfileGetters.getId(profile));
        artistTimelineEventRepository.delete(event);
    }

    private ArtistTimelineEvent requireOwnedEvent(Long eventId, Long artistId) {
        ArtistTimelineEvent event = artistTimelineEventRepository.findById(eventId)
                .orElseThrow(ArtistTimelineEventNotFoundException::new);

        if (!ArtistTimelineEventDomain.belongsToArtist(event, artistId)) {
            throw new ForbiddenProfileAccessException();
        }

        return event;
    }

    private String normalizeOptional(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}

package com.musicplatform.backend_core.artist.timeline.mapper;

import com.musicplatform.backend_core.artist.timeline.dto.response.ArtistTimelineEventResponse;
import com.musicplatform.backend_core.artist.timeline.entity.ArtistTimelineEvent;
import com.musicplatform.backend_core.artist.timeline.entity.ArtistTimelineEventGetters;

public final class ArtistTimelineEventMapper {

    private ArtistTimelineEventMapper() {
    }

    public static ArtistTimelineEventResponse toResponse(ArtistTimelineEvent event) {
        return new ArtistTimelineEventResponse(
                ArtistTimelineEventGetters.getId(event),
                ArtistTimelineEventGetters.getArtistId(event),
                ArtistTimelineEventGetters.getEventTitle(event),
                ArtistTimelineEventGetters.getEventDescription(event),
                ArtistTimelineEventGetters.getEventType(event),
                ArtistTimelineEventGetters.getEventDate(event),
                ArtistTimelineEventGetters.getEventImageUrl(event),
                ArtistTimelineEventGetters.getOrderPosition(event),
                ArtistTimelineEventGetters.getCreatedAt(event)
        );
    }
}

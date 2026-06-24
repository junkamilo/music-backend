package com.musicplatform.backend_core.artist.timeline.dto.response;

import com.musicplatform.backend_core.artist.timeline.entity.TimelineEventType;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ArtistTimelineEventResponse(
        Long id,
        Long artistId,
        String eventTitle,
        String eventDescription,
        TimelineEventType eventType,
        LocalDate eventDate,
        String eventImageUrl,
        Integer orderPosition,
        LocalDateTime createdAt
) {
}

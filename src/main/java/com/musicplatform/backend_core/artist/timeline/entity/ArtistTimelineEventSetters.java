package com.musicplatform.backend_core.artist.timeline.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class ArtistTimelineEventSetters {

    private ArtistTimelineEventSetters() {
    }

    public static void setArtistId(ArtistTimelineEvent event, Long artistId) {
        event.artistId = artistId;
    }

    public static void setEventTitle(ArtistTimelineEvent event, String eventTitle) {
        event.eventTitle = eventTitle;
    }

    public static void setEventDescription(ArtistTimelineEvent event, String eventDescription) {
        event.eventDescription = eventDescription;
    }

    public static void setEventType(ArtistTimelineEvent event, TimelineEventType eventType) {
        event.eventType = eventType;
    }

    public static void setEventDate(ArtistTimelineEvent event, LocalDate eventDate) {
        event.eventDate = eventDate;
    }

    public static void setEventImageUrl(ArtistTimelineEvent event, String eventImageUrl) {
        event.eventImageUrl = eventImageUrl;
    }

    public static void setOrderPosition(ArtistTimelineEvent event, Integer orderPosition) {
        event.orderPosition = orderPosition;
    }

    public static void setCreatedAt(ArtistTimelineEvent event, LocalDateTime createdAt) {
        event.createdAt = createdAt;
    }
}

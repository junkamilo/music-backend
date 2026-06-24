package com.musicplatform.backend_core.artist.timeline.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class ArtistTimelineEventGetters {

    private ArtistTimelineEventGetters() {
    }

    public static Long getId(ArtistTimelineEvent event) {
        return event.id;
    }

    public static Long getArtistId(ArtistTimelineEvent event) {
        return event.artistId;
    }

    public static String getEventTitle(ArtistTimelineEvent event) {
        return event.eventTitle;
    }

    public static String getEventDescription(ArtistTimelineEvent event) {
        return event.eventDescription;
    }

    public static TimelineEventType getEventType(ArtistTimelineEvent event) {
        return event.eventType;
    }

    public static LocalDate getEventDate(ArtistTimelineEvent event) {
        return event.eventDate;
    }

    public static String getEventImageUrl(ArtistTimelineEvent event) {
        return event.eventImageUrl;
    }

    public static Integer getOrderPosition(ArtistTimelineEvent event) {
        return event.orderPosition;
    }

    public static LocalDateTime getCreatedAt(ArtistTimelineEvent event) {
        return event.createdAt;
    }
}

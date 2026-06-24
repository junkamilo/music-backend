package com.musicplatform.backend_core.artist.timeline.entity;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "artist_timeline_events")
@Access(AccessType.FIELD)
public class ArtistTimelineEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "artist_id", nullable = false)
    Long artistId;

    @Column(name = "event_title", nullable = false, length = 200)
    String eventTitle;

    @Column(name = "event_description", columnDefinition = "TEXT")
    String eventDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 30)
    TimelineEventType eventType;

    @Column(name = "event_date", nullable = false)
    LocalDate eventDate;

    @Column(name = "event_image_url", length = 500)
    String eventImageUrl;

    @Column(name = "order_position")
    Integer orderPosition;

    @Column(name = "created_at", insertable = false, updatable = false)
    LocalDateTime createdAt;

    protected ArtistTimelineEvent() {
    }
}

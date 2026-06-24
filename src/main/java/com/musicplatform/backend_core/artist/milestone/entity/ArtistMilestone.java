package com.musicplatform.backend_core.artist.milestone.entity;

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
@Table(name = "artist_milestones")
@Access(AccessType.FIELD)
public class ArtistMilestone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "artist_id", nullable = false)
    Long artistId;

    @Column(nullable = false, length = 200)
    String title;

    @Column(columnDefinition = "TEXT")
    String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "milestone_type", nullable = false, length = 30)
    MilestoneType milestoneType;

    @Column(name = "date_achieved", nullable = false)
    LocalDate dateAchieved;

    @Column(name = "image_url", length = 500)
    String imageUrl;

    @Column(name = "order_position")
    Integer orderPosition;

    @Column(name = "created_at", insertable = false, updatable = false)
    LocalDateTime createdAt;

    protected ArtistMilestone() {
    }
}

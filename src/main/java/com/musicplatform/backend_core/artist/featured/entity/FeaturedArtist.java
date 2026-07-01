package com.musicplatform.backend_core.artist.featured.entity;

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
import java.time.LocalDateTime;

@Entity
@Table(name = "featured_artists")
@Access(AccessType.FIELD)
public class FeaturedArtist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "spotify_id", nullable = false, unique = true, length = 100)
    String spotifyId;

    @Column(name = "stage_name", nullable = false, length = 100)
    String stageName;

    @Column(name = "image_url", length = 500)
    String imageUrl;

    @Column(name = "spotify_url", length = 500)
    String spotifyUrl;

    @Column
    Integer popularity;

    @Column(length = 255)
    String genres;

    @Enumerated(EnumType.STRING)
    @Column(name = "sync_status", nullable = false, length = 20)
    SyncStatus syncStatus = SyncStatus.PENDING;

    @Column(name = "last_synced_at")
    LocalDateTime lastSyncedAt;

    @Column(name = "created_at", insertable = false, updatable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    LocalDateTime updatedAt;

    protected FeaturedArtist() {
    }
}

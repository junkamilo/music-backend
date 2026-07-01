package com.musicplatform.backend_core.artist.featured.entity;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "spotify_top_artists_seed")
@Access(AccessType.FIELD)
public class SpotifyTopArtistSeed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "spotify_id", nullable = false, unique = true, length = 100)
    String spotifyId;

    @Column(name = "stage_name", nullable = false, length = 100)
    String stageName;

    @Column(name = "last_synced_at", nullable = false)
    LocalDateTime lastSyncedAt;

    @Column(name = "sync_priority", nullable = false)
    Integer syncPriority = 1;

    @Column(name = "created_at", insertable = false, updatable = false)
    LocalDateTime createdAt;

    protected SpotifyTopArtistSeed() {
    }
}

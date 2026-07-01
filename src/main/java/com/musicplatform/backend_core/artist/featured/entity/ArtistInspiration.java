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
@Table(name = "artist_inspirations")
@Access(AccessType.FIELD)
public class ArtistInspiration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "emergent_artist_id", nullable = false)
    Long emergentArtistId;

    @Column(name = "featured_artist_id", nullable = false)
    Long featuredArtistId;

    @Column(name = "similarity_reason", length = 200)
    String similarityReason;

    @Column(name = "created_at", insertable = false, updatable = false)
    LocalDateTime createdAt;

    protected ArtistInspiration() {
    }
}

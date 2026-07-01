package com.musicplatform.backend_core.artist.featured.entity;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "featured_artists_curated")
@Access(AccessType.FIELD)
public class FeaturedArtistCurated {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "featured_artist_id", nullable = false)
    FeaturedArtist featuredArtist;

    @Column(name = "curator_reason", length = 255)
    String curatorReason;

    @Column(name = "spotlight_order", nullable = false)
    Integer spotlightOrder = 1;

    @Column(name = "is_active", nullable = false)
    boolean active = true;

    @Column(name = "featured_from", nullable = false)
    LocalDate featuredFrom;

    @Column(name = "featured_until")
    LocalDate featuredUntil;

    @Column(name = "created_at", insertable = false, updatable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    LocalDateTime updatedAt;

    protected FeaturedArtistCurated() {
    }
}

package com.musicplatform.backend_core.artist.entity;

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
@Table(name = "artist_profiles")
@Access(AccessType.FIELD)
public class ArtistProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "user_id", unique = true)
    Long userId;

    @Column(name = "stage_name", nullable = false, length = 100)
    String stageName;

    @Column(name = "stage_name_origin", columnDefinition = "TEXT")
    String stageNameOrigin;

    @Column(columnDefinition = "TEXT")
    String bio;

    @Column(name = "avatar_url", length = 500)
    String avatarUrl;

    @Column(name = "banner_url", length = 500)
    String bannerUrl;

    @Column(name = "birth_date")
    LocalDate birthDate;

    @Column(name = "birth_city", length = 100)
    String birthCity;

    @Column(name = "birth_country", length = 100)
    String birthCountry;

    @Column(name = "current_city", length = 100)
    String currentCity;

    @Column(name = "current_country", length = 100)
    String currentCountry;

    @Column(name = "website_url", length = 500)
    String websiteUrl;

    @Column(name = "instagram_url", length = 500)
    String instagramUrl;

    @Column(name = "spotify_url", length = 500)
    String spotifyUrl;

    @Column(name = "spotify_id", unique = true, length = 100)
    String spotifyId;

    @Enumerated(EnumType.STRING)
    @Column(name = "artist_type", nullable = false, length = 20)
    ArtistType artistType = ArtistType.INDEPENDENT;

    @Column(name = "is_verified", nullable = false)
    boolean verified = false;

    @Column(name = "is_active", nullable = false)
    boolean active = true;

    @Column(name = "created_at", insertable = false, updatable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    LocalDateTime updatedAt;

    protected ArtistProfile() {
    }
}

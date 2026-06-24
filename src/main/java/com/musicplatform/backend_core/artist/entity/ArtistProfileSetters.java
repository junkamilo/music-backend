package com.musicplatform.backend_core.artist.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class ArtistProfileSetters {

    private ArtistProfileSetters() {
    }

    public static void setUserId(ArtistProfile profile, Long userId) {
        profile.userId = userId;
    }

    public static void setStageName(ArtistProfile profile, String stageName) {
        profile.stageName = stageName;
    }

    public static void setStageNameOrigin(ArtistProfile profile, String stageNameOrigin) {
        profile.stageNameOrigin = stageNameOrigin;
    }

    public static void setBio(ArtistProfile profile, String bio) {
        profile.bio = bio;
    }

    public static void setAvatarUrl(ArtistProfile profile, String avatarUrl) {
        profile.avatarUrl = avatarUrl;
    }

    public static void setBannerUrl(ArtistProfile profile, String bannerUrl) {
        profile.bannerUrl = bannerUrl;
    }

    public static void setBirthDate(ArtistProfile profile, LocalDate birthDate) {
        profile.birthDate = birthDate;
    }

    public static void setBirthCity(ArtistProfile profile, String birthCity) {
        profile.birthCity = birthCity;
    }

    public static void setBirthCountry(ArtistProfile profile, String birthCountry) {
        profile.birthCountry = birthCountry;
    }

    public static void setCurrentCity(ArtistProfile profile, String currentCity) {
        profile.currentCity = currentCity;
    }

    public static void setCurrentCountry(ArtistProfile profile, String currentCountry) {
        profile.currentCountry = currentCountry;
    }

    public static void setWebsiteUrl(ArtistProfile profile, String websiteUrl) {
        profile.websiteUrl = websiteUrl;
    }

    public static void setInstagramUrl(ArtistProfile profile, String instagramUrl) {
        profile.instagramUrl = instagramUrl;
    }

    public static void setSpotifyUrl(ArtistProfile profile, String spotifyUrl) {
        profile.spotifyUrl = spotifyUrl;
    }

    public static void setSpotifyId(ArtistProfile profile, String spotifyId) {
        profile.spotifyId = spotifyId;
    }

    public static void setArtistType(ArtistProfile profile, ArtistType artistType) {
        profile.artistType = artistType;
    }

    public static void setVerified(ArtistProfile profile, boolean verified) {
        profile.verified = verified;
    }

    public static void setActive(ArtistProfile profile, boolean active) {
        profile.active = active;
    }

    public static void setCreatedAt(ArtistProfile profile, LocalDateTime createdAt) {
        profile.createdAt = createdAt;
    }

    public static void setUpdatedAt(ArtistProfile profile, LocalDateTime updatedAt) {
        profile.updatedAt = updatedAt;
    }
}

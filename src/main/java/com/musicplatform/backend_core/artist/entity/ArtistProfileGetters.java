package com.musicplatform.backend_core.artist.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class ArtistProfileGetters {

    private ArtistProfileGetters() {
    }

    public static Long getId(ArtistProfile profile) {
        return profile.id;
    }

    public static Long getUserId(ArtistProfile profile) {
        return profile.userId;
    }

    public static String getStageName(ArtistProfile profile) {
        return profile.stageName;
    }

    public static String getStageNameOrigin(ArtistProfile profile) {
        return profile.stageNameOrigin;
    }

    public static String getBio(ArtistProfile profile) {
        return profile.bio;
    }

    public static String getAvatarUrl(ArtistProfile profile) {
        return profile.avatarUrl;
    }

    public static String getBannerUrl(ArtistProfile profile) {
        return profile.bannerUrl;
    }

    public static LocalDate getBirthDate(ArtistProfile profile) {
        return profile.birthDate;
    }

    public static String getBirthCity(ArtistProfile profile) {
        return profile.birthCity;
    }

    public static String getBirthCountry(ArtistProfile profile) {
        return profile.birthCountry;
    }

    public static String getCurrentCity(ArtistProfile profile) {
        return profile.currentCity;
    }

    public static String getCurrentCountry(ArtistProfile profile) {
        return profile.currentCountry;
    }

    public static String getWebsiteUrl(ArtistProfile profile) {
        return profile.websiteUrl;
    }

    public static String getInstagramUrl(ArtistProfile profile) {
        return profile.instagramUrl;
    }

    public static String getSpotifyUrl(ArtistProfile profile) {
        return profile.spotifyUrl;
    }

    public static String getSpotifyId(ArtistProfile profile) {
        return profile.spotifyId;
    }

    public static ArtistType getArtistType(ArtistProfile profile) {
        return profile.artistType;
    }

    public static boolean isVerified(ArtistProfile profile) {
        return profile.verified;
    }

    public static boolean isActive(ArtistProfile profile) {
        return profile.active;
    }

    public static LocalDateTime getCreatedAt(ArtistProfile profile) {
        return profile.createdAt;
    }

    public static LocalDateTime getUpdatedAt(ArtistProfile profile) {
        return profile.updatedAt;
    }
}

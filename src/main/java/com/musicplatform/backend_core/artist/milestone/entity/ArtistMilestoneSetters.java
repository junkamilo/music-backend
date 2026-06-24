package com.musicplatform.backend_core.artist.milestone.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class ArtistMilestoneSetters {

    private ArtistMilestoneSetters() {
    }

    public static void setArtistId(ArtistMilestone milestone, Long artistId) {
        milestone.artistId = artistId;
    }

    public static void setTitle(ArtistMilestone milestone, String title) {
        milestone.title = title;
    }

    public static void setDescription(ArtistMilestone milestone, String description) {
        milestone.description = description;
    }

    public static void setMilestoneType(ArtistMilestone milestone, MilestoneType milestoneType) {
        milestone.milestoneType = milestoneType;
    }

    public static void setDateAchieved(ArtistMilestone milestone, LocalDate dateAchieved) {
        milestone.dateAchieved = dateAchieved;
    }

    public static void setImageUrl(ArtistMilestone milestone, String imageUrl) {
        milestone.imageUrl = imageUrl;
    }

    public static void setOrderPosition(ArtistMilestone milestone, Integer orderPosition) {
        milestone.orderPosition = orderPosition;
    }

    public static void setCreatedAt(ArtistMilestone milestone, LocalDateTime createdAt) {
        milestone.createdAt = createdAt;
    }
}

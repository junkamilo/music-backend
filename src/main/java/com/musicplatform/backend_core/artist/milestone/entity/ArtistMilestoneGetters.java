package com.musicplatform.backend_core.artist.milestone.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

public final class ArtistMilestoneGetters {

    private ArtistMilestoneGetters() {
    }

    public static Long getId(ArtistMilestone milestone) {
        return milestone.id;
    }

    public static Long getArtistId(ArtistMilestone milestone) {
        return milestone.artistId;
    }

    public static String getTitle(ArtistMilestone milestone) {
        return milestone.title;
    }

    public static String getDescription(ArtistMilestone milestone) {
        return milestone.description;
    }

    public static MilestoneType getMilestoneType(ArtistMilestone milestone) {
        return milestone.milestoneType;
    }

    public static LocalDate getDateAchieved(ArtistMilestone milestone) {
        return milestone.dateAchieved;
    }

    public static String getImageUrl(ArtistMilestone milestone) {
        return milestone.imageUrl;
    }

    public static Integer getOrderPosition(ArtistMilestone milestone) {
        return milestone.orderPosition;
    }

    public static LocalDateTime getCreatedAt(ArtistMilestone milestone) {
        return milestone.createdAt;
    }
}

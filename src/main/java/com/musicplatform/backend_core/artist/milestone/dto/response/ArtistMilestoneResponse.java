package com.musicplatform.backend_core.artist.milestone.dto.response;

import com.musicplatform.backend_core.artist.milestone.entity.MilestoneType;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ArtistMilestoneResponse(
        Long id,
        Long artistId,
        String title,
        String description,
        MilestoneType milestoneType,
        LocalDate dateAchieved,
        String imageUrl,
        Integer orderPosition,
        LocalDateTime createdAt
) {
}

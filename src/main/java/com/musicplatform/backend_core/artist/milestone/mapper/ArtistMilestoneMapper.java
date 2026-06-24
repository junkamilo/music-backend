package com.musicplatform.backend_core.artist.milestone.mapper;

import com.musicplatform.backend_core.artist.milestone.dto.response.ArtistMilestoneResponse;
import com.musicplatform.backend_core.artist.milestone.entity.ArtistMilestone;
import com.musicplatform.backend_core.artist.milestone.entity.ArtistMilestoneGetters;

public final class ArtistMilestoneMapper {

    private ArtistMilestoneMapper() {
    }

    public static ArtistMilestoneResponse toResponse(ArtistMilestone milestone) {
        return new ArtistMilestoneResponse(
                ArtistMilestoneGetters.getId(milestone),
                ArtistMilestoneGetters.getArtistId(milestone),
                ArtistMilestoneGetters.getTitle(milestone),
                ArtistMilestoneGetters.getDescription(milestone),
                ArtistMilestoneGetters.getMilestoneType(milestone),
                ArtistMilestoneGetters.getDateAchieved(milestone),
                ArtistMilestoneGetters.getImageUrl(milestone),
                ArtistMilestoneGetters.getOrderPosition(milestone),
                ArtistMilestoneGetters.getCreatedAt(milestone)
        );
    }
}

package com.musicplatform.backend_core.artist.milestone.domain;

import com.musicplatform.backend_core.artist.milestone.entity.ArtistMilestone;
import com.musicplatform.backend_core.artist.milestone.entity.ArtistMilestoneGetters;

public final class ArtistMilestoneDomain {

    private ArtistMilestoneDomain() {
    }

    public static boolean belongsToArtist(ArtistMilestone milestone, Long artistId) {
        return ArtistMilestoneGetters.getArtistId(milestone).equals(artistId);
    }
}

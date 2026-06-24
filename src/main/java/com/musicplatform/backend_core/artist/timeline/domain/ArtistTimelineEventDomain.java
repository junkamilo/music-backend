package com.musicplatform.backend_core.artist.timeline.domain;

import com.musicplatform.backend_core.artist.timeline.entity.ArtistTimelineEvent;
import com.musicplatform.backend_core.artist.timeline.entity.ArtistTimelineEventGetters;

public final class ArtistTimelineEventDomain {

    private ArtistTimelineEventDomain() {
    }

    public static boolean belongsToArtist(ArtistTimelineEvent event, Long artistId) {
        return ArtistTimelineEventGetters.getArtistId(event).equals(artistId);
    }
}

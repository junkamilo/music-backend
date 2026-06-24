package com.musicplatform.backend_core.artist.timeline.entity;

public final class ArtistTimelineEventFactory {

    private ArtistTimelineEventFactory() {
    }

    public static ArtistTimelineEvent createEmpty() {
        return new ArtistTimelineEvent();
    }
}

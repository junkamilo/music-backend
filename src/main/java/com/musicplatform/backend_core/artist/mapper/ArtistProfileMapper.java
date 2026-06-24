package com.musicplatform.backend_core.artist.mapper;

import com.musicplatform.backend_core.artist.dto.response.ArtistProfileResponse;
import com.musicplatform.backend_core.artist.dto.response.ArtistPublicProfileResponse;
import com.musicplatform.backend_core.artist.dto.response.FeaturedArtistSummary;
import com.musicplatform.backend_core.artist.entity.ArtistProfile;
import com.musicplatform.backend_core.artist.entity.ArtistProfileGetters;
import com.musicplatform.backend_core.artist.milestone.dto.response.ArtistMilestoneResponse;
import com.musicplatform.backend_core.artist.timeline.dto.response.ArtistTimelineEventResponse;
import java.util.List;

public final class ArtistProfileMapper {

    private ArtistProfileMapper() {
    }

    public static ArtistProfileResponse toResponse(ArtistProfile profile) {
        return new ArtistProfileResponse(
                ArtistProfileGetters.getId(profile),
                ArtistProfileGetters.getUserId(profile),
                ArtistProfileGetters.getStageName(profile),
                ArtistProfileGetters.getStageNameOrigin(profile),
                ArtistProfileGetters.getBio(profile),
                ArtistProfileGetters.getAvatarUrl(profile),
                ArtistProfileGetters.getBannerUrl(profile),
                ArtistProfileGetters.getBirthDate(profile),
                ArtistProfileGetters.getBirthCity(profile),
                ArtistProfileGetters.getBirthCountry(profile),
                ArtistProfileGetters.getCurrentCity(profile),
                ArtistProfileGetters.getCurrentCountry(profile),
                ArtistProfileGetters.getWebsiteUrl(profile),
                ArtistProfileGetters.getInstagramUrl(profile),
                ArtistProfileGetters.getSpotifyUrl(profile),
                ArtistProfileGetters.getSpotifyId(profile),
                ArtistProfileGetters.getArtistType(profile),
                ArtistProfileGetters.isVerified(profile),
                ArtistProfileGetters.isActive(profile),
                ArtistProfileGetters.getCreatedAt(profile),
                ArtistProfileGetters.getUpdatedAt(profile)
        );
    }

    public static ArtistPublicProfileResponse toPublicResponse(
            ArtistProfile profile,
            List<ArtistMilestoneResponse> milestones,
            List<ArtistTimelineEventResponse> timelineEvents
    ) {
        ArtistProfileResponse base = toResponse(profile);
        return new ArtistPublicProfileResponse(
                base.id(),
                base.userId(),
                base.stageName(),
                base.stageNameOrigin(),
                base.bio(),
                base.avatarUrl(),
                base.bannerUrl(),
                base.birthDate(),
                base.birthCity(),
                base.birthCountry(),
                base.currentCity(),
                base.currentCountry(),
                base.websiteUrl(),
                base.instagramUrl(),
                base.spotifyUrl(),
                base.spotifyId(),
                base.artistType(),
                base.verified(),
                base.active(),
                base.createdAt(),
                base.updatedAt(),
                milestones,
                timelineEvents
        );
    }

    public static FeaturedArtistSummary toFeaturedSummary(ArtistProfile profile) {
        return new FeaturedArtistSummary(
                ArtistProfileGetters.getId(profile),
                ArtistProfileGetters.getStageName(profile),
                ArtistProfileGetters.getAvatarUrl(profile),
                ArtistProfileGetters.getSpotifyId(profile),
                ArtistProfileGetters.getSpotifyUrl(profile),
                ArtistProfileGetters.getArtistType(profile)
        );
    }
}

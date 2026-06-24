package com.musicplatform.backend_core.artist.domain;

import com.musicplatform.backend_core.artist.entity.ArtistProfile;
import com.musicplatform.backend_core.artist.entity.ArtistProfileGetters;
import com.musicplatform.backend_core.artist.entity.ArtistProfileSetters;
import com.musicplatform.backend_core.artist.entity.ArtistType;

public final class ArtistProfileDomain {

    private ArtistProfileDomain() {
    }

    public static boolean isActive(ArtistProfile profile) {
        return ArtistProfileGetters.isActive(profile);
    }

    public static boolean isVerified(ArtistProfile profile) {
        return ArtistProfileGetters.isVerified(profile);
    }

    public static boolean isFeatured(ArtistProfile profile) {
        return ArtistProfileGetters.getArtistType(profile) == ArtistType.FEATURED;
    }

    public static boolean isIndependent(ArtistProfile profile) {
        return ArtistProfileGetters.getArtistType(profile) == ArtistType.INDEPENDENT;
    }

    public static boolean isUnclaimedFeatured(ArtistProfile profile) {
        return isFeatured(profile) && ArtistProfileGetters.getUserId(profile) == null;
    }

    public static boolean canBeDisplayedPublicly(ArtistProfile profile) {
        return ArtistProfileGetters.isActive(profile);
    }

    public static boolean canBeEditedBy(ArtistProfile profile, Long userId) {
        Long ownerId = ArtistProfileGetters.getUserId(profile);
        return ownerId != null && ownerId.equals(userId);
    }

    public static void claimAsIndependent(ArtistProfile profile, Long userId) {
        ArtistProfileSetters.setUserId(profile, userId);
        ArtistProfileSetters.setArtistType(profile, ArtistType.INDEPENDENT);
    }

    public static void deactivate(ArtistProfile profile) {
        ArtistProfileSetters.setActive(profile, false);
    }

    public static void markVerified(ArtistProfile profile) {
        ArtistProfileSetters.setVerified(profile, true);
    }

    public static void revokeVerification(ArtistProfile profile) {
        ArtistProfileSetters.setVerified(profile, false);
    }
}

package com.musicplatform.backend_core.artist.dto.response;

import com.musicplatform.backend_core.artist.entity.ArtistType;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ArtistProfileResponse(
        Long id,
        Long userId,
        String stageName,
        String stageNameOrigin,
        String bio,
        String avatarUrl,
        String bannerUrl,
        LocalDate birthDate,
        String birthCity,
        String birthCountry,
        String currentCity,
        String currentCountry,
        String websiteUrl,
        String instagramUrl,
        String spotifyUrl,
        String spotifyId,
        ArtistType artistType,
        boolean verified,
        boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

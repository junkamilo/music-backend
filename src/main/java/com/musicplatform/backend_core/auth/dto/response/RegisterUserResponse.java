package com.musicplatform.backend_core.auth.dto.response;

import com.musicplatform.backend_core.artist.dto.response.ArtistProfileResponse;
import com.musicplatform.backend_core.shared.enums.UserRole;
import java.time.LocalDateTime;

public record RegisterUserResponse(
        Long id,
        String username,
        String email,
        UserRole role,
        boolean active,
        LocalDateTime createdAt,
        ArtistProfileResponse artist
) {
}

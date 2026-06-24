package com.musicplatform.backend_core.user.dto.response;

import com.musicplatform.backend_core.shared.enums.UserRole;
import java.time.LocalDateTime;

public record UserProfileResponse(
        Long id,
        String username,
        String email,
        UserRole role,
        boolean active,
        boolean emailVerified,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

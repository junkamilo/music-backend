package com.musicplatform.backend_core.auth.dto.response;

import com.musicplatform.backend_core.shared.enums.UserRole;

public record AuthUserSummary(
        Long id,
        String username,
        String email,
        UserRole role
) {
}

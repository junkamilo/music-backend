package com.musicplatform.backend_core.shared.security;

import com.musicplatform.backend_core.shared.enums.UserRole;

public record UserPrincipal(Long userId, UserRole role) {
}

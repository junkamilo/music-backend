package com.musicplatform.backend_core.auth.domain;

import com.musicplatform.backend_core.auth.entity.RefreshToken;
import com.musicplatform.backend_core.auth.entity.RefreshTokenGetters;
import com.musicplatform.backend_core.auth.entity.RefreshTokenSetters;
import java.time.LocalDateTime;

public final class RefreshTokenDomain {

    private RefreshTokenDomain() {
    }

    public static boolean isExpired(RefreshToken token) {
        return RefreshTokenGetters.getExpiresAt(token).isBefore(LocalDateTime.now());
    }

    public static boolean isRevoked(RefreshToken token) {
        return RefreshTokenGetters.getRevokedAt(token) != null;
    }

    public static boolean isValid(RefreshToken token) {
        return !isExpired(token) && !isRevoked(token);
    }

    public static void revoke(RefreshToken token, LocalDateTime revokedAt) {
        RefreshTokenSetters.setRevokedAt(token, revokedAt);
    }
}

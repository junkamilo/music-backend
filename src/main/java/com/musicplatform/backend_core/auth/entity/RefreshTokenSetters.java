package com.musicplatform.backend_core.auth.entity;

import java.time.LocalDateTime;

public final class RefreshTokenSetters {

    private RefreshTokenSetters() {
    }

    public static void setId(RefreshToken token, Long id) {
        token.id = id;
    }

    public static void setUserId(RefreshToken token, Long userId) {
        token.userId = userId;
    }

    public static void setTokenHash(RefreshToken token, String tokenHash) {
        token.tokenHash = tokenHash;
    }

    public static void setDeviceInfo(RefreshToken token, String deviceInfo) {
        token.deviceInfo = deviceInfo;
    }

    public static void setIpAddress(RefreshToken token, String ipAddress) {
        token.ipAddress = ipAddress;
    }

    public static void setExpiresAt(RefreshToken token, LocalDateTime expiresAt) {
        token.expiresAt = expiresAt;
    }

    public static void setRevokedAt(RefreshToken token, LocalDateTime revokedAt) {
        token.revokedAt = revokedAt;
    }

    public static void setCreatedAt(RefreshToken token, LocalDateTime createdAt) {
        token.createdAt = createdAt;
    }
}

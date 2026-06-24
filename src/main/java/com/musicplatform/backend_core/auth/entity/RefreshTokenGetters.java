package com.musicplatform.backend_core.auth.entity;

import java.time.LocalDateTime;

public final class RefreshTokenGetters {

    private RefreshTokenGetters() {
    }

    public static Long getId(RefreshToken token) {
        return token.id;
    }

    public static Long getUserId(RefreshToken token) {
        return token.userId;
    }

    public static String getTokenHash(RefreshToken token) {
        return token.tokenHash;
    }

    public static String getDeviceInfo(RefreshToken token) {
        return token.deviceInfo;
    }

    public static String getIpAddress(RefreshToken token) {
        return token.ipAddress;
    }

    public static LocalDateTime getExpiresAt(RefreshToken token) {
        return token.expiresAt;
    }

    public static LocalDateTime getRevokedAt(RefreshToken token) {
        return token.revokedAt;
    }

    public static LocalDateTime getCreatedAt(RefreshToken token) {
        return token.createdAt;
    }
}

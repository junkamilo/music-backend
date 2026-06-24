package com.musicplatform.backend_core.auth.entity;

import java.time.LocalDateTime;

public final class EmailVerificationTokenGetters {

    private EmailVerificationTokenGetters() {
    }

    public static Long getId(EmailVerificationToken token) {
        return token.id;
    }

    public static Long getUserId(EmailVerificationToken token) {
        return token.userId;
    }

    public static String getToken(EmailVerificationToken token) {
        return token.token;
    }

    public static String getVerificationCode(EmailVerificationToken token) {
        return token.verificationCode;
    }

    public static LocalDateTime getExpiresAt(EmailVerificationToken token) {
        return token.expiresAt;
    }

    public static LocalDateTime getVerifiedAt(EmailVerificationToken token) {
        return token.verifiedAt;
    }

    public static LocalDateTime getCreatedAt(EmailVerificationToken token) {
        return token.createdAt;
    }
}

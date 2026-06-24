package com.musicplatform.backend_core.auth.entity;

import java.time.LocalDateTime;

public final class EmailVerificationTokenSetters {

    private EmailVerificationTokenSetters() {
    }

    public static void setUserId(EmailVerificationToken token, Long userId) {
        token.userId = userId;
    }

    public static void setToken(EmailVerificationToken token, String value) {
        token.token = value;
    }

    public static void setVerificationCode(EmailVerificationToken token, String verificationCode) {
        token.verificationCode = verificationCode;
    }

    public static void setExpiresAt(EmailVerificationToken token, LocalDateTime expiresAt) {
        token.expiresAt = expiresAt;
    }

    public static void setVerifiedAt(EmailVerificationToken token, LocalDateTime verifiedAt) {
        token.verifiedAt = verifiedAt;
    }
}

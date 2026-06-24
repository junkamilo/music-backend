package com.musicplatform.backend_core.auth.domain;

import com.musicplatform.backend_core.auth.entity.EmailVerificationToken;
import com.musicplatform.backend_core.auth.entity.EmailVerificationTokenGetters;
import com.musicplatform.backend_core.auth.entity.EmailVerificationTokenSetters;
import java.time.LocalDateTime;

public final class EmailVerificationTokenDomain {

    private EmailVerificationTokenDomain() {
    }

    public static boolean isExpired(EmailVerificationToken token) {
        return EmailVerificationTokenGetters.getExpiresAt(token).isBefore(LocalDateTime.now());
    }

    public static boolean isVerified(EmailVerificationToken token) {
        return EmailVerificationTokenGetters.getVerifiedAt(token) != null;
    }

    public static boolean isUsable(EmailVerificationToken token) {
        return !isExpired(token) && !isVerified(token);
    }

    public static void markVerified(EmailVerificationToken token, LocalDateTime verifiedAt) {
        EmailVerificationTokenSetters.setVerifiedAt(token, verifiedAt);
    }
}

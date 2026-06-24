package com.musicplatform.backend_core.auth.entity;

public final class EmailVerificationTokenFactory {

    private EmailVerificationTokenFactory() {
    }

    public static EmailVerificationToken createEmpty() {
        return new EmailVerificationToken();
    }
}

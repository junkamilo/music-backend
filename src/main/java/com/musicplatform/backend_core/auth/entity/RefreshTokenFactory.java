package com.musicplatform.backend_core.auth.entity;

public final class RefreshTokenFactory {

    private RefreshTokenFactory() {
    }

    public static RefreshToken createEmpty() {
        return new RefreshToken();
    }
}

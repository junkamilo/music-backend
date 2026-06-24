package com.musicplatform.backend_core.auth.entity;

public final class UserFactory {

    private UserFactory() {
    }

    public static User createEmpty() {
        return new User();
    }
}

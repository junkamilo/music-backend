package com.musicplatform.backend_core.user.mapper;

import com.musicplatform.backend_core.auth.entity.User;
import com.musicplatform.backend_core.auth.entity.UserGetters;
import com.musicplatform.backend_core.user.dto.response.UserProfileResponse;

public final class UserProfileMapper {

    private UserProfileMapper() {
    }

    public static UserProfileResponse toResponse(User user) {
        return new UserProfileResponse(
                UserGetters.getId(user),
                UserGetters.getUsername(user),
                UserGetters.getEmail(user),
                UserGetters.getRole(user),
                UserGetters.isActive(user),
                UserGetters.getEmailVerifiedAt(user) != null,
                UserGetters.getCreatedAt(user),
                UserGetters.getUpdatedAt(user)
        );
    }
}

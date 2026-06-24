package com.musicplatform.backend_core.auth.mapper;

import com.musicplatform.backend_core.artist.dto.response.ArtistProfileResponse;
import com.musicplatform.backend_core.auth.dto.response.AuthUserSummary;
import com.musicplatform.backend_core.auth.dto.response.RegisterUserResponse;
import com.musicplatform.backend_core.auth.entity.User;
import com.musicplatform.backend_core.auth.entity.UserGetters;

public final class UserMapper {

    private UserMapper() {
    }

    public static AuthUserSummary toAuthUserSummary(User user) {
        return new AuthUserSummary(
                UserGetters.getId(user),
                UserGetters.getUsername(user),
                UserGetters.getEmail(user),
                UserGetters.getRole(user)
        );
    }

    public static RegisterUserResponse toRegisterResponse(User user, ArtistProfileResponse artist) {
        return new RegisterUserResponse(
                UserGetters.getId(user),
                UserGetters.getUsername(user),
                UserGetters.getEmail(user),
                UserGetters.getRole(user),
                UserGetters.isActive(user),
                UserGetters.getCreatedAt(user),
                artist
        );
    }
}

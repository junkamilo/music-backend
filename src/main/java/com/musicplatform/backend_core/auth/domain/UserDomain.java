package com.musicplatform.backend_core.auth.domain;

import com.musicplatform.backend_core.auth.entity.User;
import com.musicplatform.backend_core.auth.entity.UserGetters;
import com.musicplatform.backend_core.auth.entity.UserSetters;
import com.musicplatform.backend_core.shared.enums.UserRole;
import java.time.LocalDateTime;

/**
 * Reglas de negocio sobre {@link User} sin acoplar la entidad JPA a la lógica de dominio.
 */
public final class UserDomain {

    private UserDomain() {
    }

    public static boolean isEmailVerified(User user) {
        return UserGetters.getEmailVerifiedAt(user) != null;
    }

    public static boolean isSoftDeleted(User user) {
        return UserGetters.getDeletedAt(user) != null;
    }

    public static boolean canAccessPlatform(User user) {
        return UserGetters.isActive(user) && !isSoftDeleted(user);
    }

    public static boolean isCreator(User user) {
        return UserGetters.getRole(user) == UserRole.CREATOR;
    }

    public static boolean isAdmin(User user) {
        return UserGetters.getRole(user) == UserRole.ADMIN;
    }

    public static boolean isListener(User user) {
        return UserGetters.getRole(user) == UserRole.LISTENER;
    }

    public static void markEmailVerified(User user, LocalDateTime verifiedAt) {
        UserSetters.setEmailVerifiedAt(user, verifiedAt);
    }

    public static void deactivate(User user) {
        UserSetters.setActive(user, false);
    }

    public static void softDelete(User user, LocalDateTime deletedAt) {
        UserSetters.setDeletedAt(user, deletedAt);
        UserSetters.setActive(user, false);
    }
}

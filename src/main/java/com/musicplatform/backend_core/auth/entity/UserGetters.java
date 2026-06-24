package com.musicplatform.backend_core.auth.entity;

import com.musicplatform.backend_core.shared.enums.UserRole;
import java.time.LocalDateTime;

/**
 * Punto único de lectura sobre {@link User}.
 * Extrae la lógica de acceso a datos fuera de la entidad principal.
 */
public final class UserGetters {

    private UserGetters() {
    }

    public static Long getId(User user) {
        return user.id;
    }

    public static String getUsername(User user) {
        return user.username;
    }

    public static String getEmail(User user) {
        return user.email;
    }

    public static String getPasswordHash(User user) {
        return user.passwordHash;
    }

    public static UserRole getRole(User user) {
        return user.role;
    }

    public static boolean isActive(User user) {
        return user.active;
    }

    public static LocalDateTime getEmailVerifiedAt(User user) {
        return user.emailVerifiedAt;
    }

    public static LocalDateTime getCreatedAt(User user) {
        return user.createdAt;
    }

    public static LocalDateTime getUpdatedAt(User user) {
        return user.updatedAt;
    }

    public static LocalDateTime getDeletedAt(User user) {
        return user.deletedAt;
    }
}

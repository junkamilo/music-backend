package com.musicplatform.backend_core.auth.entity;

import com.musicplatform.backend_core.shared.enums.UserRole;
import java.time.LocalDateTime;

/**
 * Punto único de escritura sobre {@link User}.
 * La entidad expone solo getters; toda mutación pasa por esta clase.
 */
public final class UserSetters {

    private UserSetters() {
    }

    public static void setId(User user, Long id) {
        user.id = id;
    }

    public static void setUsername(User user, String username) {
        user.username = username;
    }

    public static void setEmail(User user, String email) {
        user.email = email;
    }

    public static void setPasswordHash(User user, String passwordHash) {
        user.passwordHash = passwordHash;
    }

    public static void setRole(User user, UserRole role) {
        user.role = role;
    }

    public static void setActive(User user, boolean active) {
        user.active = active;
    }

    public static void setEmailVerifiedAt(User user, LocalDateTime emailVerifiedAt) {
        user.emailVerifiedAt = emailVerifiedAt;
    }

    public static void setCreatedAt(User user, LocalDateTime createdAt) {
        user.createdAt = createdAt;
    }

    public static void setUpdatedAt(User user, LocalDateTime updatedAt) {
        user.updatedAt = updatedAt;
    }

    public static void setDeletedAt(User user, LocalDateTime deletedAt) {
        user.deletedAt = deletedAt;
    }
}

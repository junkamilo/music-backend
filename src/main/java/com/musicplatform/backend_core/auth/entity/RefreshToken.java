package com.musicplatform.backend_core.auth.entity;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
@Access(AccessType.FIELD)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "user_id", nullable = false)
    Long userId;

    @Column(name = "token_hash", nullable = false, length = 255)
    String tokenHash;

    @Column(name = "device_info", length = 255)
    String deviceInfo;

    @Column(name = "ip_address", length = 45)
    String ipAddress;

    @Column(name = "expires_at", nullable = false)
    LocalDateTime expiresAt;

    @Column(name = "revoked_at")
    LocalDateTime revokedAt;

    @Column(name = "created_at", insertable = false, updatable = false)
    LocalDateTime createdAt;

    protected RefreshToken() {
    }
}

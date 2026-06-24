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
@Table(name = "email_verification_tokens")
@Access(AccessType.FIELD)
public class EmailVerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "user_id", nullable = false)
    Long userId;

    @Column(nullable = false, unique = true, length = 255)
    String token;

    @Column(name = "verification_code", nullable = false, unique = true, length = 6)
    String verificationCode;

    @Column(name = "expires_at", nullable = false)
    LocalDateTime expiresAt;

    @Column(name = "verified_at")
    LocalDateTime verifiedAt;

    @Column(name = "created_at", insertable = false, updatable = false)
    LocalDateTime createdAt;

    protected EmailVerificationToken() {
    }
}

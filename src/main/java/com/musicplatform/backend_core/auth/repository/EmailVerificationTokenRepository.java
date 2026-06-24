package com.musicplatform.backend_core.auth.repository;

import com.musicplatform.backend_core.auth.entity.EmailVerificationToken;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {

    Optional<EmailVerificationToken> findByToken(String token);

    Optional<EmailVerificationToken> findByVerificationCode(String verificationCode);

    @Query("""
            SELECT t FROM EmailVerificationToken t
            WHERE t.userId = :userId AND t.verifiedAt IS NULL
            """)
    List<EmailVerificationToken> findActiveByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM EmailVerificationToken t WHERE t.userId = :userId")
    void deleteByUserId(@Param("userId") Long userId);

    boolean existsByVerificationCode(String verificationCode);
}

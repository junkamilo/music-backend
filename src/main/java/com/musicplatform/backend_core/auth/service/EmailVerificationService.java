package com.musicplatform.backend_core.auth.service;

import com.musicplatform.backend_core.auth.domain.EmailVerificationTokenDomain;
import com.musicplatform.backend_core.auth.domain.UserDomain;
import com.musicplatform.backend_core.auth.entity.EmailVerificationToken;
import com.musicplatform.backend_core.auth.entity.EmailVerificationTokenFactory;
import com.musicplatform.backend_core.auth.entity.EmailVerificationTokenGetters;
import com.musicplatform.backend_core.auth.entity.EmailVerificationTokenSetters;
import com.musicplatform.backend_core.auth.entity.User;
import com.musicplatform.backend_core.auth.entity.UserGetters;
import com.musicplatform.backend_core.auth.exception.EmailAlreadyVerifiedException;
import com.musicplatform.backend_core.auth.exception.InvalidVerificationTokenException;
import com.musicplatform.backend_core.auth.exception.ResendVerificationTooSoonException;
import com.musicplatform.backend_core.auth.exception.VerificationTokenExpiredException;
import com.musicplatform.backend_core.auth.repository.EmailVerificationTokenRepository;
import com.musicplatform.backend_core.auth.repository.UserRepository;
import com.musicplatform.backend_core.config.EmailProperties;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmailVerificationService {

    private static final String CODE_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int CODE_LENGTH = 6;
    private static final int RESEND_COOLDOWN_SECONDS = 60;

    private final EmailVerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final EmailProperties emailProperties;
    private final SecureRandom secureRandom = new SecureRandom();

    public EmailVerificationService(
            EmailVerificationTokenRepository tokenRepository,
            UserRepository userRepository,
            EmailService emailService,
            EmailProperties emailProperties
    ) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.emailProperties = emailProperties;
    }

    @Transactional
    public void issueVerification(User user) {
        if (UserDomain.isEmailVerified(user)) {
            throw new EmailAlreadyVerifiedException();
        }

        invalidateForUser(UserGetters.getId(user));

        EmailVerificationToken token = EmailVerificationTokenFactory.createEmpty();
        EmailVerificationTokenSetters.setUserId(token, UserGetters.getId(user));
        EmailVerificationTokenSetters.setToken(token, UUID.randomUUID().toString());
        EmailVerificationTokenSetters.setVerificationCode(token, generateUniqueCode());
        EmailVerificationTokenSetters.setExpiresAt(
                token,
                LocalDateTime.now().plusHours(emailProperties.getVerificationExpiryHours())
        );

        EmailVerificationToken saved = tokenRepository.save(token);

        emailService.sendVerificationEmail(
                UserGetters.getUsername(user),
                UserGetters.getEmail(user),
                EmailVerificationTokenGetters.getVerificationCode(saved),
                EmailVerificationTokenGetters.getToken(saved)
        );
    }

    @Transactional
    public void resendByEmail(String email) {
        String normalized = email.trim().toLowerCase();
        Optional<User> userOptional = userRepository.findByEmail(normalized);
        if (userOptional.isEmpty()) {
            return;
        }

        User user = userOptional.get();
        if (UserDomain.isEmailVerified(user)) {
            return;
        }

        enforceResendCooldown(UserGetters.getId(user));
        issueVerification(user);
    }

    @Transactional
    public void verifyByToken(String tokenValue) {
        EmailVerificationToken token = tokenRepository.findByToken(tokenValue.trim())
                .orElseThrow(InvalidVerificationTokenException::new);
        completeVerification(token);
    }

    @Transactional
    public void verifyByCode(String code) {
        String normalized = code.trim().toUpperCase();
        EmailVerificationToken token = tokenRepository.findByVerificationCode(normalized)
                .orElseThrow(InvalidVerificationTokenException::new);
        completeVerification(token);
    }

    @Transactional
    public void invalidateForUser(Long userId) {
        tokenRepository.deleteByUserId(userId);
    }

    private void completeVerification(EmailVerificationToken token) {
        if (EmailVerificationTokenDomain.isVerified(token)) {
            throw new InvalidVerificationTokenException();
        }

        if (EmailVerificationTokenDomain.isExpired(token)) {
            throw new VerificationTokenExpiredException();
        }

        User user = userRepository.findById(EmailVerificationTokenGetters.getUserId(token))
                .orElseThrow(InvalidVerificationTokenException::new);

        if (UserDomain.isEmailVerified(user)) {
            tokenRepository.deleteByUserId(UserGetters.getId(user));
            throw new EmailAlreadyVerifiedException();
        }

        UserDomain.markEmailVerified(user, LocalDateTime.now());
        userRepository.save(user);

        EmailVerificationTokenDomain.markVerified(token, LocalDateTime.now());
        tokenRepository.save(token);
        tokenRepository.deleteByUserId(UserGetters.getId(user));
    }

    private void enforceResendCooldown(Long userId) {
        List<EmailVerificationToken> activeTokens = tokenRepository.findActiveByUserId(userId);
        if (activeTokens.isEmpty()) {
            return;
        }

        LocalDateTime createdAt = EmailVerificationTokenGetters.getCreatedAt(activeTokens.getFirst());
        if (createdAt == null) {
            return;
        }

        if (createdAt.plusSeconds(RESEND_COOLDOWN_SECONDS).isAfter(LocalDateTime.now())) {
            throw new ResendVerificationTooSoonException();
        }
    }

    private String generateUniqueCode() {
        for (int attempt = 0; attempt < 20; attempt++) {
            StringBuilder builder = new StringBuilder(CODE_LENGTH);
            for (int i = 0; i < CODE_LENGTH; i++) {
                builder.append(CODE_CHARS.charAt(secureRandom.nextInt(CODE_CHARS.length())));
            }
            String code = builder.toString();
            if (!tokenRepository.existsByVerificationCode(code)) {
                return code;
            }
        }
        throw new IllegalStateException("No se pudo generar un código de verificación único");
    }
}

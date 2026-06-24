package com.musicplatform.backend_core.auth.service;

import com.musicplatform.backend_core.auth.domain.RefreshTokenDomain;
import com.musicplatform.backend_core.auth.entity.RefreshToken;
import com.musicplatform.backend_core.auth.entity.RefreshTokenFactory;
import com.musicplatform.backend_core.auth.entity.RefreshTokenGetters;
import com.musicplatform.backend_core.auth.entity.RefreshTokenSetters;
import com.musicplatform.backend_core.auth.entity.User;
import com.musicplatform.backend_core.auth.entity.UserGetters;
import com.musicplatform.backend_core.auth.exception.InvalidRefreshTokenException;
import com.musicplatform.backend_core.auth.repository.RefreshTokenRepository;
import com.musicplatform.backend_core.config.JwtProperties;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenHashService tokenHashService;
    private final JwtProperties jwtProperties;
    private final SecureRandom secureRandom = new SecureRandom();

    public RefreshTokenService(
            RefreshTokenRepository refreshTokenRepository,
            TokenHashService tokenHashService,
            JwtProperties jwtProperties
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenHashService = tokenHashService;
        this.jwtProperties = jwtProperties;
    }

    public IssuedRefreshToken issueForUser(User user, String deviceInfo, String ipAddress) {
        String rawToken = generateRawToken();
        RefreshToken refreshToken = buildToken(UserGetters.getId(user), rawToken, deviceInfo, ipAddress);
        refreshTokenRepository.save(refreshToken);
        return new IssuedRefreshToken(rawToken, refreshToken);
    }

    @Transactional
    public IssuedRefreshToken rotate(RefreshToken existingToken, String deviceInfo, String ipAddress) {
        RefreshTokenDomain.revoke(existingToken, LocalDateTime.now());
        refreshTokenRepository.save(existingToken);

        String rawToken = generateRawToken();
        RefreshToken refreshToken = buildToken(
                RefreshTokenGetters.getUserId(existingToken),
                rawToken,
                deviceInfo,
                ipAddress
        );
        refreshTokenRepository.save(refreshToken);
        return new IssuedRefreshToken(rawToken, refreshToken);
    }

    @Transactional
    public RefreshToken validateRawToken(String rawToken) {
        String tokenHash = tokenHashService.hash(rawToken);
        RefreshToken refreshToken = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(InvalidRefreshTokenException::new);

        if (!RefreshTokenDomain.isValid(refreshToken)) {
            throw new InvalidRefreshTokenException();
        }

        return refreshToken;
    }

    @Transactional
    public void revokeRawToken(String rawToken) {
        String tokenHash = tokenHashService.hash(rawToken);
        RefreshToken refreshToken = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(InvalidRefreshTokenException::new);

        if (!RefreshTokenDomain.isRevoked(refreshToken)) {
            RefreshTokenDomain.revoke(refreshToken, LocalDateTime.now());
            refreshTokenRepository.save(refreshToken);
        }
    }

    private RefreshToken buildToken(Long userId, String rawToken, String deviceInfo, String ipAddress) {
        RefreshToken refreshToken = RefreshTokenFactory.createEmpty();
        RefreshTokenSetters.setUserId(refreshToken, userId);
        RefreshTokenSetters.setTokenHash(refreshToken, tokenHashService.hash(rawToken));
        RefreshTokenSetters.setDeviceInfo(refreshToken, deviceInfo);
        RefreshTokenSetters.setIpAddress(refreshToken, ipAddress);
        RefreshTokenSetters.setExpiresAt(
                refreshToken,
                LocalDateTime.now().plusDays(jwtProperties.getRefreshExpirationDays())
        );
        return refreshToken;
    }

    private String generateRawToken() {
        byte[] bytes = new byte[64];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public record IssuedRefreshToken(String rawToken, RefreshToken entity) {
    }
}

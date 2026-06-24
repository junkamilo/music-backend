package com.musicplatform.backend_core.auth.service;

import com.musicplatform.backend_core.artist.dto.response.ArtistProfileResponse;
import com.musicplatform.backend_core.artist.service.ArtistProfileService;
import com.musicplatform.backend_core.auth.domain.UserDomain;
import com.musicplatform.backend_core.auth.dto.request.LoginRequest;
import com.musicplatform.backend_core.auth.dto.request.RefreshTokenRequest;
import com.musicplatform.backend_core.auth.dto.request.RegisterUserRequest;
import com.musicplatform.backend_core.auth.dto.response.LoginResponse;
import com.musicplatform.backend_core.auth.dto.response.RegisterUserResponse;
import com.musicplatform.backend_core.auth.entity.RefreshToken;
import com.musicplatform.backend_core.auth.entity.RefreshTokenGetters;
import com.musicplatform.backend_core.auth.entity.User;
import com.musicplatform.backend_core.auth.entity.UserFactory;
import com.musicplatform.backend_core.auth.entity.UserGetters;
import com.musicplatform.backend_core.auth.entity.UserSetters;
import com.musicplatform.backend_core.auth.exception.AccountInactiveException;
import com.musicplatform.backend_core.auth.exception.EmailAlreadyExistsException;
import com.musicplatform.backend_core.auth.exception.InvalidCredentialsException;
import com.musicplatform.backend_core.auth.exception.UsernameAlreadyExistsException;
import com.musicplatform.backend_core.auth.mapper.UserMapper;
import com.musicplatform.backend_core.auth.repository.UserRepository;
import com.musicplatform.backend_core.shared.enums.UserRole;
import com.musicplatform.backend_core.shared.util.JwtUtil;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final EmailVerificationService emailVerificationService;
    private final ArtistProfileService artistProfileService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            RefreshTokenService refreshTokenService,
            EmailVerificationService emailVerificationService,
            ArtistProfileService artistProfileService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
        this.emailVerificationService = emailVerificationService;
        this.artistProfileService = artistProfileService;
    }

    @Transactional
    public RegisterUserResponse register(RegisterUserRequest request) {
        String username = request.username().trim();
        String email = request.email().trim().toLowerCase();

        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException(email);
        }

        if (userRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyExistsException(username);
        }

        User user = UserFactory.createEmpty();
        UserSetters.setUsername(user, username);
        UserSetters.setEmail(user, email);
        UserSetters.setPasswordHash(user, passwordEncoder.encode(request.password()));
        UserSetters.setRole(user, UserRole.LISTENER);

        User savedUser = userRepository.save(user);
        emailVerificationService.issueVerification(savedUser);

        Optional<ArtistProfileResponse> claimedArtist = artistProfileService.claimFeaturedProfileOnRegister(
                UserGetters.getId(savedUser),
                request.stageName(),
                request.spotifyId()
        );

        User refreshedUser = userRepository.findById(UserGetters.getId(savedUser))
                .orElse(savedUser);

        return UserMapper.toRegisterResponse(refreshedUser, claimedArtist.orElse(null));
    }

    @Transactional
    public LoginResponse login(LoginRequest request, String deviceInfo, String ipAddress) {
        User user = findByIdentifier(request.identifier())
                .orElseThrow(InvalidCredentialsException::new);

        if (!UserDomain.canAccessPlatform(user)) {
            throw new AccountInactiveException();
        }

        if (!passwordEncoder.matches(request.password(), UserGetters.getPasswordHash(user))) {
            throw new InvalidCredentialsException();
        }

        return buildLoginResponse(user, deviceInfo, ipAddress);
    }

    @Transactional
    public LoginResponse refresh(RefreshTokenRequest request, String deviceInfo, String ipAddress) {
        RefreshToken existingToken = refreshTokenService.validateRawToken(request.refreshToken());
        User user = userRepository.findById(RefreshTokenGetters.getUserId(existingToken))
                .orElseThrow(InvalidCredentialsException::new);

        if (!UserDomain.canAccessPlatform(user)) {
            throw new AccountInactiveException();
        }

        RefreshTokenService.IssuedRefreshToken rotated = refreshTokenService.rotate(
                existingToken,
                deviceInfo,
                ipAddress
        );

        return new LoginResponse(
                jwtUtil.generateAccessToken(user),
                rotated.rawToken(),
                "Bearer",
                jwtUtil.getAccessExpirationSeconds(),
                UserMapper.toAuthUserSummary(user)
        );
    }

    @Transactional
    public void logout(RefreshTokenRequest request) {
        refreshTokenService.revokeRawToken(request.refreshToken());
    }

    private LoginResponse buildLoginResponse(User user, String deviceInfo, String ipAddress) {
        RefreshTokenService.IssuedRefreshToken issuedRefreshToken =
                refreshTokenService.issueForUser(user, deviceInfo, ipAddress);

        return new LoginResponse(
                jwtUtil.generateAccessToken(user),
                issuedRefreshToken.rawToken(),
                "Bearer",
                jwtUtil.getAccessExpirationSeconds(),
                UserMapper.toAuthUserSummary(user)
        );
    }

    private Optional<User> findByIdentifier(String identifier) {
        String normalized = identifier.trim();
        if (normalized.contains("@")) {
            return userRepository.findByEmail(normalized.toLowerCase());
        }
        return userRepository.findByUsername(normalized);
    }
}

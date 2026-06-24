package com.musicplatform.backend_core.user.service;

import com.musicplatform.backend_core.auth.domain.UserDomain;
import com.musicplatform.backend_core.auth.dto.response.AuthUserSummary;
import com.musicplatform.backend_core.auth.entity.User;
import com.musicplatform.backend_core.auth.entity.UserGetters;
import com.musicplatform.backend_core.auth.entity.UserSetters;
import com.musicplatform.backend_core.auth.exception.AccountInactiveException;
import com.musicplatform.backend_core.auth.exception.EmailAlreadyExistsException;
import com.musicplatform.backend_core.auth.exception.InvalidCredentialsException;
import com.musicplatform.backend_core.auth.exception.UsernameAlreadyExistsException;
import com.musicplatform.backend_core.auth.mapper.UserMapper;
import com.musicplatform.backend_core.auth.repository.UserRepository;
import com.musicplatform.backend_core.auth.service.EmailVerificationService;
import com.musicplatform.backend_core.shared.enums.UserRole;
import com.musicplatform.backend_core.user.dto.request.ChangePasswordRequest;
import com.musicplatform.backend_core.user.dto.request.UpdateUserProfileRequest;
import com.musicplatform.backend_core.user.dto.response.UserProfileResponse;
import com.musicplatform.backend_core.user.mapper.UserProfileMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationService emailVerificationService;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            EmailVerificationService emailVerificationService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailVerificationService = emailVerificationService;
    }

    public AuthUserSummary getById(Long userId) {
        User user = requireActiveUser(userId);
        return UserMapper.toAuthUserSummary(user);
    }

    public UserProfileResponse getMyProfile(Long userId) {
        User user = requireActiveUser(userId);
        return UserProfileMapper.toResponse(user);
    }

    @Transactional
    public UserProfileResponse updateMyProfile(Long userId, UpdateUserProfileRequest request) {
        User user = requireActiveUser(userId);

        String username = normalizeOptionalText(request.username());
        String email = normalizeOptionalEmail(request.email());

        if (username == null && email == null) {
            return UserProfileMapper.toResponse(user);
        }

        boolean emailChanged = false;

        if (username != null) {
            String currentUsername = UserGetters.getUsername(user);
            if (!username.equals(currentUsername)
                    && userRepository.findByUsername(username).isPresent()) {
                throw new UsernameAlreadyExistsException(username);
            }
            UserSetters.setUsername(user, username);
        }

        if (email != null) {
            String currentEmail = UserGetters.getEmail(user);
            if (!email.equals(currentEmail)
                    && userRepository.findByEmail(email).isPresent()) {
                throw new EmailAlreadyExistsException(email);
            }
            if (!email.equals(currentEmail)) {
                UserSetters.setEmail(user, email);
                UserSetters.setEmailVerifiedAt(user, null);
                emailVerificationService.invalidateForUser(UserGetters.getId(user));
                emailChanged = true;
            }
        }

        User savedUser = userRepository.save(user);

        if (emailChanged) {
            emailVerificationService.issueVerification(savedUser);
        }

        return UserProfileMapper.toResponse(savedUser);
    }

    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = requireActiveUser(userId);

        if (!passwordEncoder.matches(request.currentPassword(), UserGetters.getPasswordHash(user))) {
            throw new InvalidCredentialsException();
        }

        UserSetters.setPasswordHash(user, passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    @Transactional
    public AuthUserSummary becomeCreator(Long userId) {
        User user = requireActiveUser(userId);

        UserRole role = UserGetters.getRole(user);
        if (role == UserRole.CREATOR || role == UserRole.ADMIN) {
            return UserMapper.toAuthUserSummary(user);
        }

        UserSetters.setRole(user, UserRole.CREATOR);
        User savedUser = userRepository.save(user);
        return UserMapper.toAuthUserSummary(savedUser);
    }

    private User requireActiveUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(InvalidCredentialsException::new);

        if (!UserDomain.canAccessPlatform(user)) {
            throw new AccountInactiveException();
        }

        return user;
    }

    private String normalizeOptionalText(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private String normalizeOptionalEmail(String value) {
        String normalized = normalizeOptionalText(value);
        return normalized == null ? null : normalized.toLowerCase();
    }
}

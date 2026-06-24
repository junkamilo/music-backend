package com.musicplatform.backend_core.user.controller;

import com.musicplatform.backend_core.auth.dto.response.AuthUserSummary;
import com.musicplatform.backend_core.shared.security.UserPrincipal;
import com.musicplatform.backend_core.user.dto.request.ChangePasswordRequest;
import com.musicplatform.backend_core.user.dto.request.UpdateUserProfileRequest;
import com.musicplatform.backend_core.user.dto.response.UserProfileResponse;
import com.musicplatform.backend_core.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> me(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return ResponseEntity.ok(userService.getMyProfile(principal.userId()));
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileResponse> updateMe(
            @Valid @RequestBody UpdateUserProfileRequest request,
            Authentication authentication
    ) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return ResponseEntity.ok(userService.updateMyProfile(principal.userId(), request));
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication
    ) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        userService.changePassword(principal.userId(), request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/me/become-creator")
    public ResponseEntity<AuthUserSummary> becomeCreator(Authentication authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        return ResponseEntity.ok(userService.becomeCreator(principal.userId()));
    }
}

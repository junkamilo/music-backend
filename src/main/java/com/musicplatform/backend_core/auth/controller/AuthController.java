package com.musicplatform.backend_core.auth.controller;

import com.musicplatform.backend_core.auth.dto.request.LoginRequest;
import com.musicplatform.backend_core.auth.dto.request.RefreshTokenRequest;
import com.musicplatform.backend_core.auth.dto.request.RegisterUserRequest;
import com.musicplatform.backend_core.auth.dto.request.ResendVerificationRequest;
import com.musicplatform.backend_core.auth.dto.request.VerifyEmailRequest;
import com.musicplatform.backend_core.auth.dto.response.LoginResponse;
import com.musicplatform.backend_core.auth.dto.response.RegisterUserResponse;
import com.musicplatform.backend_core.auth.service.AuthService;
import com.musicplatform.backend_core.auth.service.EmailVerificationService;
import com.musicplatform.backend_core.config.EmailProperties;
import com.musicplatform.backend_core.shared.dto.MessageResponse;
import com.musicplatform.backend_core.shared.util.RequestUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final EmailVerificationService emailVerificationService;
    private final EmailProperties emailProperties;

    public AuthController(
            AuthService authService,
            EmailVerificationService emailVerificationService,
            EmailProperties emailProperties
    ) {
        this.authService = authService;
        this.emailVerificationService = emailVerificationService;
        this.emailProperties = emailProperties;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        RegisterUserResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest
    ) {
        LoginResponse response = authService.login(
                request,
                RequestUtils.resolveDeviceInfo(httpRequest),
                RequestUtils.resolveClientIp(httpRequest)
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh(
            @Valid @RequestBody RefreshTokenRequest request,
            HttpServletRequest httpRequest
    ) {
        LoginResponse response = authService.refresh(
                request,
                RequestUtils.resolveDeviceInfo(httpRequest),
                RequestUtils.resolveClientIp(httpRequest)
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequest request) {
        authService.logout(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<Void> resendVerification(@Valid @RequestBody ResendVerificationRequest request) {
        emailVerificationService.resendByEmail(request.email());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/verify-email")
    public ResponseEntity<MessageResponse> verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        emailVerificationService.verifyByCode(request.code());
        return ResponseEntity.ok(new MessageResponse("Email verificado correctamente"));
    }

    @GetMapping("/verify")
    public void verifyByLink(
            @RequestParam("token") String token,
            HttpServletResponse response
    ) throws IOException {
        String base = emailProperties.getFrontendBaseUrl().replaceAll("/$", "");
        try {
            emailVerificationService.verifyByToken(token);
            response.sendRedirect(base + "/verify-email?status=success");
        } catch (Exception ex) {
            String reason = URLEncoder.encode(ex.getMessage(), StandardCharsets.UTF_8);
            response.sendRedirect(base + "/verify-email?status=error&reason=" + reason);
        }
    }
}

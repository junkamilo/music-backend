package com.musicplatform.backend_core.auth.service;

import com.musicplatform.backend_core.auth.service.email.EmailSender;
import com.musicplatform.backend_core.config.EmailProperties;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

@Service
public class EmailService {

    private final EmailSender emailSender;
    private final EmailProperties emailProperties;

    public EmailService(EmailSender emailSender, EmailProperties emailProperties) {
        this.emailSender = emailSender;
        this.emailProperties = emailProperties;
    }

    public void sendVerificationEmail(String username, String email, String code, String token) {
        String verificationLink = buildVerificationLink(token);
        String expiryHours = String.valueOf(emailProperties.getVerificationExpiryHours());

        String textBody = renderTemplate("templates/email/verification.txt", username, code, verificationLink, expiryHours);
        String htmlBody = renderTemplate("templates/email/verification.html", username, code, verificationLink, expiryHours);

        emailSender.sendVerificationEmail(
                email,
                "Verifica tu correo — Music Platform",
                htmlBody,
                textBody
        );
    }

    public String buildVerificationLink(String token) {
        String base = emailProperties.getFrontendBaseUrl().replaceAll("/$", "");
        return base + "/verify-email?token=" + token;
    }

    public String buildBackendVerifyLink(String token) {
        return "/api/v1/auth/verify?token=" + token;
    }

    private String renderTemplate(
            String path,
            String username,
            String code,
            String verificationLink,
            String expiryHours
    ) {
        try {
            String template = StreamUtils.copyToString(
                    new ClassPathResource(path).getInputStream(),
                    StandardCharsets.UTF_8
            );
            return template
                    .replace("{{username}}", username)
                    .replace("{{code}}", code)
                    .replace("{{verificationLink}}", verificationLink)
                    .replace("{{expiryHours}}", expiryHours);
        } catch (IOException ex) {
            throw new IllegalStateException("No se pudo cargar la plantilla de email: " + path, ex);
        }
    }
}

package com.musicplatform.backend_core.auth.service.email;

public interface EmailSender {

    void sendVerificationEmail(
            String toEmail,
            String subject,
            String htmlBody,
            String textBody
    );
}

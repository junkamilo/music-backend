package com.musicplatform.backend_core.auth.service.email;

import com.musicplatform.backend_core.config.EmailProperties;
import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.email.provider", havingValue = "resend")
public class ResendEmailSender implements EmailSender {

    private final EmailProperties emailProperties;

    public ResendEmailSender(EmailProperties emailProperties) {
        this.emailProperties = emailProperties;
    }

    @Override
    public void sendVerificationEmail(
            String toEmail,
            String subject,
            String htmlBody,
            String textBody
    ) {
        Resend resend = new Resend(emailProperties.getResendApiKey());
        CreateEmailOptions options = CreateEmailOptions.builder()
                .from(emailProperties.getFrom())
                .to(toEmail)
                .subject(subject)
                .html(htmlBody)
                .text(textBody)
                .build();

        try {
            resend.emails().send(options);
        } catch (Exception ex) {
            throw new IllegalStateException("No se pudo enviar el correo de verificación", ex);
        }
    }
}

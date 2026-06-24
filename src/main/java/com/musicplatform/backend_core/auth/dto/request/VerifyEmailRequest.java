package com.musicplatform.backend_core.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record VerifyEmailRequest(

        @NotBlank(message = "El código es obligatorio")
        @Size(min = 6, max = 6, message = "El código debe tener 6 caracteres")
        @Pattern(regexp = "^[A-Za-z0-9]{6}$", message = "El código solo puede contener letras y números")
        String code
) {
}

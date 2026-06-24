package com.musicplatform.backend_core.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserProfileRequest(

        @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
        @Pattern(
                regexp = "^[a-zA-Z0-9_]+$",
                message = "El nombre de usuario solo puede contener letras, números y guion bajo"
        )
        String username,

        @Email(message = "El formato del correo no es válido")
        @Size(max = 100, message = "El correo no puede superar 100 caracteres")
        String email
) {
}

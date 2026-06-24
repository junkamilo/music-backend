package com.musicplatform.backend_core.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(

        @NotBlank(message = "La contraseña actual es obligatoria")
        @Size(min = 8, max = 72, message = "La contraseña actual debe tener entre 8 y 72 caracteres")
        String currentPassword,

        @NotBlank(message = "La nueva contraseña es obligatoria")
        @Size(min = 8, max = 72, message = "La nueva contraseña debe tener entre 8 y 72 caracteres")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
                message = "La nueva contraseña debe incluir al menos una letra y un número"
        )
        String newPassword
) {
}

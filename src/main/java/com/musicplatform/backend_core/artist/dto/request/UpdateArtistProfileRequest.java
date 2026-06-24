package com.musicplatform.backend_core.artist.dto.request;

import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record UpdateArtistProfileRequest(

        @Size(min = 3, max = 100, message = "El nombre artístico debe tener entre 3 y 100 caracteres")
        String stageName,

        @Size(max = 5000, message = "La historia del nombre no puede superar 5000 caracteres")
        String stageNameOrigin,

        @Size(max = 5000, message = "La biografía no puede superar 5000 caracteres")
        String bio,

        @Size(max = 500, message = "La URL del avatar no puede superar 500 caracteres")
        String avatarUrl,

        @Size(max = 500, message = "La URL del banner no puede superar 500 caracteres")
        String bannerUrl,

        LocalDate birthDate,

        @Size(max = 100, message = "La ciudad de nacimiento no puede superar 100 caracteres")
        String birthCity,

        @Size(max = 100, message = "El país de nacimiento no puede superar 100 caracteres")
        String birthCountry,

        @Size(max = 100, message = "La ciudad actual no puede superar 100 caracteres")
        String currentCity,

        @Size(max = 100, message = "El país actual no puede superar 100 caracteres")
        String currentCountry,

        @Size(max = 500, message = "La URL del sitio web no puede superar 500 caracteres")
        String websiteUrl,

        @Size(max = 500, message = "La URL de Instagram no puede superar 500 caracteres")
        String instagramUrl,

        @Size(max = 500, message = "La URL de Spotify no puede superar 500 caracteres")
        String spotifyUrl
) {
}

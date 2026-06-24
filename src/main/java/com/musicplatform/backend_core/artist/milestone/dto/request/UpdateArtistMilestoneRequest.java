package com.musicplatform.backend_core.artist.milestone.dto.request;

import com.musicplatform.backend_core.artist.milestone.entity.MilestoneType;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record UpdateArtistMilestoneRequest(

        @Size(max = 200, message = "El título no puede superar 200 caracteres")
        String title,

        @Size(max = 5000, message = "La descripción no puede superar 5000 caracteres")
        String description,

        MilestoneType milestoneType,

        LocalDate dateAchieved,

        @Size(max = 500, message = "La URL de la imagen no puede superar 500 caracteres")
        String imageUrl,

        Integer orderPosition
) {
}

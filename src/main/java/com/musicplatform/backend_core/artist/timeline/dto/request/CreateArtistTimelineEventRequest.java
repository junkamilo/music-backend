package com.musicplatform.backend_core.artist.timeline.dto.request;

import com.musicplatform.backend_core.artist.timeline.entity.TimelineEventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record CreateArtistTimelineEventRequest(

        @NotBlank(message = "El título del evento es obligatorio")
        @Size(max = 200, message = "El título no puede superar 200 caracteres")
        String eventTitle,

        @Size(max = 10000, message = "La descripción no puede superar 10000 caracteres")
        String eventDescription,

        @NotNull(message = "El tipo de evento es obligatorio")
        TimelineEventType eventType,

        @NotNull(message = "La fecha del evento es obligatoria")
        LocalDate eventDate,

        @Size(max = 500, message = "La URL de la imagen no puede superar 500 caracteres")
        String eventImageUrl,

        Integer orderPosition
) {
}

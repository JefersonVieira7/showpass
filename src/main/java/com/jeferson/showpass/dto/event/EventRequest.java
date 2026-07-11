package com.jeferson.showpass.dto.event;

import com.jeferson.showpass.domain.enums.EventType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record EventRequest(
        @NotBlank(message = "Name is required")
        String name,

        String description,

        @NotNull(message = "Event date is required")
        @Future(message = "Event date must be in the future")
        LocalDateTime eventDate,

        String venue,

        @NotNull(message = "Event type is required")
        EventType type
) {
}
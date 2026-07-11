package com.jeferson.showpass.dto.event;

import com.jeferson.showpass.domain.enums.EventType;

import java.time.LocalDateTime;

public record EventResponse(
        Long id,
        String name,
        String description,
        LocalDateTime eventDate,
        String venue,
        EventType type,
        LocalDateTime createdAt
) {
}

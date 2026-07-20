package com.jeferson.showpass.dto.reservation;

import com.jeferson.showpass.domain.enums.ReservationStatus;

import java.time.LocalDateTime;

public record ReservationResponse(
        Long id,
        Long ticketId,
        String ticketType,
        Long eventId,
        ReservationStatus status,
        LocalDateTime createdAt
) {
}
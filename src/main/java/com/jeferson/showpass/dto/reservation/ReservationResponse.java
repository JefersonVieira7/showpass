package com.jeferson.showpass.dto.reservation;

import com.jeferson.showpass.domain.entity.Reservation;
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
    public static ReservationResponse fromEntity(Reservation reservation){
        return new ReservationResponse(
                reservation.getId(),
                reservation.getTicket().getId(),
                reservation.getTicket().getType(),
                reservation.getTicket().getEvent().getId(),
                reservation.getStatus(),
                reservation.getCreatedAt()

        );
    }
}

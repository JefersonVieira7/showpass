package com.jeferson.showpass.dto.reservation;

import jakarta.validation.constraints.NotNull;

public record ReservationRequest(
        @NotNull(message = "Ticket id is required")
        Long ticketId
) {
}
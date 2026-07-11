package com.jeferson.showpass.dto.ticket;

import java.util.List;

public record EventAvailabilityResponse(
        Long eventId,
        String eventName,
        List<TicketResponse> tickets
) {
}
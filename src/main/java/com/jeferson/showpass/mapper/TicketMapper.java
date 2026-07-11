package com.jeferson.showpass.mapper;

import com.jeferson.showpass.domain.entity.Ticket;
import com.jeferson.showpass.dto.ticket.TicketResponse;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {

    public TicketResponse toResponse(Ticket ticket) {
        return new TicketResponse(
                ticket.getId(),
                ticket.getType(),
                ticket.getPrice(),
                ticket.getTotalQuantity(),
                ticket.getAvailableQuantity()
        );
    }
}
package com.jeferson.showpass.dto.ticket;

import java.math.BigDecimal;

public record TicketResponse(
        Long id,
        String type,
        BigDecimal price,
        Integer totalQuantity,
        Integer availableQuantity
) {
}
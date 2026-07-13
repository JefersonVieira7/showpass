package com.jeferson.showpass.service;

import com.jeferson.showpass.domain.entity.Event;
import com.jeferson.showpass.domain.entity.Ticket;
import com.jeferson.showpass.dto.ticket.EventAvailabilityResponse;
import com.jeferson.showpass.dto.ticket.TicketRequest;
import com.jeferson.showpass.dto.ticket.TicketResponse;
import com.jeferson.showpass.mapper.TicketMapper;
import com.jeferson.showpass.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final EventService eventService;

    @Transactional
    public TicketResponse create(Long eventId, TicketRequest request) {
        Event event = eventService.findEventOrThrow(eventId);

        Ticket ticket = Ticket.builder()
                .event(event)
                .type(request.type())
                .price(request.price())
                .totalQuantity(request.totalQuantity())
                .availableQuantity(request.totalQuantity())
                .build();

        Ticket saved = ticketRepository.save(ticket);
        return ticketMapper.toResponse(saved);
    }

    public EventAvailabilityResponse getAvailability(Long eventId) {
        Event event = eventService.findEventOrThrow(eventId);

        List<TicketResponse> tickets = ticketRepository.findByEventId(eventId).stream()
                .map(ticketMapper::toResponse)
                .toList();

        return new EventAvailabilityResponse(event.getId(), event.getName(), tickets);
    }
}
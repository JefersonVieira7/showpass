package com.jeferson.showpass.controller;

import com.jeferson.showpass.dto.event.EventRequest;
import com.jeferson.showpass.dto.event.EventResponse;
import com.jeferson.showpass.dto.ticket.EventAvailabilityResponse;
import com.jeferson.showpass.dto.ticket.TicketRequest;
import com.jeferson.showpass.dto.ticket.TicketResponse;
import com.jeferson.showpass.service.EventService;
import com.jeferson.showpass.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<EventResponse> create(@Valid @RequestBody EventRequest request) {
        EventResponse response = eventService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> findAll() {
        return ResponseEntity.ok(eventService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.findById(id));
    }

    @PostMapping("/{id}/tickets")
    public ResponseEntity<TicketResponse> createTicket(@PathVariable Long id,
                                                       @Valid @RequestBody TicketRequest request) {
        TicketResponse response = ticketService.create(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}/availability")
    public ResponseEntity<EventAvailabilityResponse> availability(@PathVariable Long id) {
        long start = System.currentTimeMillis();
        EventAvailabilityResponse response = ticketService.getAvailability(id);
        long elapsedMs = System.currentTimeMillis() - start;
        log.info("GET /events/{}/availability resolved in {} ms", id, elapsedMs);
        return ResponseEntity.ok(response);
    }
}
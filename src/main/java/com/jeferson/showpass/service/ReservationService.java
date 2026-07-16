package com.jeferson.showpass.service;

import com.jeferson.showpass.domain.entity.Reservation;
import com.jeferson.showpass.domain.entity.Ticket;
import com.jeferson.showpass.domain.entity.User;
import com.jeferson.showpass.domain.enums.ReservationStatus;
import com.jeferson.showpass.dto.reservation.ReservationRequest;
import com.jeferson.showpass.dto.reservation.ReservationResponse;
import com.jeferson.showpass.exception.ResourceNotFoundException;
import com.jeferson.showpass.exception.TicketSoldOutException;
import com.jeferson.showpass.repository.ReservationRepository;
import com.jeferson.showpass.repository.TicketRepository;
import com.jeferson.showpass.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public ReservationResponse create(String userEmail, ReservationRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));

        Ticket ticket = ticketRepository.findByIdForUpdate(request.ticketId())
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found: " + request.ticketId()));

        if (ticket.getAvailableQuantity() <= 0) {
            throw new TicketSoldOutException(ticket.getId());
        }

        ticket.setAvailableQuantity(ticket.getAvailableQuantity() - 1);
        ticketRepository.save(ticket);

        Reservation reservation = Reservation.builder()
                .user(user)
                .ticket(ticket)
                .status(ReservationStatus.CONFIRMED)
                .build();

        Reservation saved = reservationRepository.save(reservation);

        return new ReservationResponse(
                saved.getId(),
                ticket.getId(),
                ticket.getType(),
                saved.getStatus(),
                saved.getCreatedAt()
        );
    }
}
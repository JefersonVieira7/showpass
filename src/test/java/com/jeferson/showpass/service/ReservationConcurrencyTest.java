package com.jeferson.showpass.service;

import com.jeferson.showpass.domain.entity.Event;
import com.jeferson.showpass.domain.entity.Ticket;
import com.jeferson.showpass.domain.entity.User;
import com.jeferson.showpass.domain.enums.EventType;
import com.jeferson.showpass.dto.reservation.ReservationRequest;
import com.jeferson.showpass.repository.EventRepository;
import com.jeferson.showpass.repository.ReservationRepository;
import com.jeferson.showpass.repository.TicketRepository;
import com.jeferson.showpass.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class ReservationConcurrencyTest {

    private static final int THREAD_COUNT = 100;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private Long ticketId;
    private String userEmail;

    @BeforeEach
    void setUp() {
        reservationRepository.deleteAll();
        ticketRepository.deleteAll();
        eventRepository.deleteAll();
        userRepository.deleteAll();

        User user = userRepository.save(User.builder()
                .name("Concurrency Test User")
                .email("concurrency-test@example.com")
                .password("not-used-in-this-test")
                .build());
        userEmail = user.getEmail();

        Event event = eventRepository.save(Event.builder()
                .name("Sold Out Show")
                .eventDate(LocalDateTime.now().plusDays(30))
                .type(EventType.CONCERT)
                .build());

        Ticket ticket = ticketRepository.save(Ticket.builder()
                .event(event)
                .type("Last One")
                .price(new BigDecimal("100.00"))
                .totalQuantity(1)
                .availableQuantity(1)
                .build());
        ticketId = ticket.getId();
    }

    @Test
    void onlyOneThreadShouldReserveTheLastTicket() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch readyLatch = new CountDownLatch(THREAD_COUNT);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(THREAD_COUNT);

        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.submit(() -> {
                readyLatch.countDown();
                try {
                    startLatch.await();
                    reservationService.create(userEmail, new ReservationRequest(ticketId));
                    successCount.incrementAndGet();
                } catch (Exception ex) {
                    failureCount.incrementAndGet();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        readyLatch.await();
        startLatch.countDown();
        boolean completed = doneLatch.await(30, TimeUnit.SECONDS);
        executor.shutdown();

        assertTrue(completed, "All threads should have completed within the timeout");
        assertEquals(1, successCount.get(), "Exactly one thread should have reserved the ticket");
        assertEquals(THREAD_COUNT - 1, failureCount.get(), "All other threads should have failed with sold out");

        Ticket finalTicket = ticketRepository.findById(ticketId).orElseThrow();
        assertEquals(0, finalTicket.getAvailableQuantity(), "Available quantity should be exactly zero, never negative");
        assertEquals(1, reservationRepository.count(), "Exactly one reservation row should exist");
    }
}
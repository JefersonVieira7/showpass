package com.jeferson.showpass.exception;

public class TicketSoldOutException extends RuntimeException {

    public TicketSoldOutException(Long ticketId) {
        super("Ticket sold out: " + ticketId);
    }
}
package com.jeferson.showpass.mapper;

import com.jeferson.showpass.domain.entity.Event;
import com.jeferson.showpass.dto.event.EventRequest;
import com.jeferson.showpass.dto.event.EventResponse;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public Event toEntity(EventRequest request) {
        return Event.builder()
                .name(request.name())
                .description(request.description())
                .eventDate(request.eventDate())
                .venue(request.venue())
                .type(request.type())
                .build();
    }

    public EventResponse toResponse(Event event) {
        return new EventResponse(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getEventDate(),
                event.getVenue(),
                event.getType(),
                event.getCreatedAt()
        );
    }
}
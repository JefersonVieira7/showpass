package com.jeferson.showpass.service;

import com.jeferson.showpass.domain.entity.Event;
import com.jeferson.showpass.dto.event.EventRequest;
import com.jeferson.showpass.dto.event.EventResponse;
import com.jeferson.showpass.exception.ResourceNotFoundException;
import com.jeferson.showpass.mapper.EventMapper;
import com.jeferson.showpass.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Transactional
    public EventResponse create(EventRequest request) {
        Event event = eventMapper.toEntity(request);
        Event saved = eventRepository.save(event);
        return eventMapper.toResponse(saved);
    }

    public List<EventResponse> findAll() {
        return eventRepository.findAll().stream()
                .map(eventMapper::toResponse)
                .toList();
    }

    public EventResponse findById(Long id) {
        return eventMapper.toResponse(findEventOrThrow(id));
    }

    public Event findEventOrThrow(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found: " + id));
    }
}
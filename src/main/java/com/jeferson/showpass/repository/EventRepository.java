package com.jeferson.showpass.repository;

import com.jeferson.showpass.domain.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
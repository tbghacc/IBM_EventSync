package com.ibm.EventSync.controller;

import com.ibm.EventSync.dto.EventDTO;
import com.ibm.EventSync.model.Event;
import com.ibm.EventSync.model.Sentiment;
import com.ibm.EventSync.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    @GetMapping("/{id}")
    public Event getEvent(@PathVariable(value = "id") UUID id) {
        return eventService.getEvent(id);
    }

    @GetMapping
    public List<Event> getEvents() {
        return eventService.getEvents();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Event createEvent(@Valid @RequestBody EventDTO event) {
        return eventService.createEvent(event);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public Event updateEvent(@PathVariable(value = "id") UUID id, @Valid @RequestBody EventDTO event) {
        return eventService.updateEvent(id, event);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEventById(@PathVariable(value = "id") UUID id) {
        eventService.deleteEventById(id);
    }

    @GetMapping("/{id}/summary")
    public Sentiment getEventSentiment(@PathVariable(value = "id") UUID id) {
        return eventService.getEventSentiment(id);
    }
}

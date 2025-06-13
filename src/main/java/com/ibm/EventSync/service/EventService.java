package com.ibm.EventSync.service;

import com.ibm.EventSync.dto.EventDTO;
import com.ibm.EventSync.model.Event;
import com.ibm.EventSync.model.Feedback;
import com.ibm.EventSync.model.Sentiment;
import com.ibm.EventSync.repository.EventRepository;
import com.ibm.EventSync.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    private final FeedbackRepository feedbackRepository;

    public Event createEvent(EventDTO eventDto) {
        Event event = Event.builder()
                .id(UUID.randomUUID())
                .title(eventDto.getTitle())
                .description(eventDto.getDescription())
                .build();
        eventRepository.insertEvent(event);
        return event;
    }

    public Event updateEvent(UUID id, EventDTO eventDto) {
        Event event = Event.builder()
                .id(id)
                .title(eventDto.getTitle())
                .description(eventDto.getDescription())
                .build();
        eventRepository.updateEvent(event);
        return event;
    }

    // I could also add a way to pull sentiment values from feedback, but then it would need a bunch of database calls
    // Also this is just more readable
    public Sentiment getEventSentiment(UUID id) {
        List<Feedback> eventFeedback = feedbackRepository.getFeedbackByEvent(id);

        double  allPositive = 0.0;
        double  allNeutral = 0.0;
        double  allNegative = 0.0;

        for (Feedback feedback : eventFeedback) {
            allPositive+= feedback.getPositive();
            allNeutral+= feedback.getNeutral();
            allNegative+= feedback.getNegative();
        }

        double feedbackAmount = eventFeedback.size();

        Sentiment sentiment = Sentiment.builder()
                .positive(allPositive/feedbackAmount)
                .neutral(allNeutral/feedbackAmount)
                .negative(allNegative/feedbackAmount)
                .build();
        return sentiment;
    }


    public List<Event> getEvents() {
        return eventRepository.getEvents();
    }

    public Event getEvent(UUID id) {
        return eventRepository.getEvent(id);
    }

    public void deleteEventById(UUID id) {
        List<Feedback> eventFeedback = feedbackRepository.getFeedbackByEvent(id);
        for (Feedback feedback: eventFeedback){
            feedbackRepository.deleteFeedback(id,feedback.getId());
        }

        eventRepository.deleteEventById(id);
    }

}

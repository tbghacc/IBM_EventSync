package com.ibm.EventSync.service;

import com.ibm.EventSync.dto.EventDTO;
import com.ibm.EventSync.exception.EntityNotFoundException;
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
        eventRepository.getEvent(id); // This is a very dirty way to see if event exists

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
        if (eventFeedback.isEmpty())
            throw (new EntityNotFoundException(String.format("Event with id: %s has no feedback", id)));

        double allPositive = 0.0;
        double allNeutral = 0.0;
        double allNegative = 0.0;

        for (Feedback feedback : eventFeedback) {
            allPositive += feedback.getPositive();
            allNeutral += feedback.getNeutral();
            allNegative += feedback.getNegative();
        }

        // Dominant sentiment
        // Set the dominant sentiment, might be useful
        String ds = allNegative > allNeutral && allNegative > allPositive ? "Negative" :
                allNeutral >= allNegative && allNeutral >= allPositive ? "Neutral" : "Positive";


        // /100 is there so when the values get divided they display as percentages
        // This is basically the same as multiplying each sentiment value by 100
        double feedbackAmount = eventFeedback.size() / 100.;

        double positive = allPositive / feedbackAmount;
        double neutral = allNeutral / feedbackAmount;
        double negative = allNegative / feedbackAmount;

        // Weighted sentiment
        // This takes into account that an event might have 50 positive reviews and 49 negative
        // In that situation it's probably more right to assume that the sentiment is more neutral
        double weightedSentiment = (positive * 2) + neutral;


        // The weighted sentiment is a value between 0 and 200, so I just used /3 as the limits
        String ws = weightedSentiment <= 67 ? "Negative" :
                weightedSentiment <= 134 ? "Neutral" : "Positive";

        // Dominant sentiment is seperated for ease of use
        Sentiment sentiment = Sentiment.builder()
                .feedback_amount(eventFeedback.size())
                .dominant_sentiment(ds)
                .weighted_sentiment(ws)
                .positive(positive)
                .neutral(neutral)
                .negative(negative)
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

        eventRepository.getEvent(id);

        List<Feedback> eventFeedback = feedbackRepository.getFeedbackByEvent(id);
        for (Feedback feedback : eventFeedback) {
            feedbackRepository.deleteFeedback(id, feedback.getId());
        }

        eventRepository.deleteEventById(id);
    }

}

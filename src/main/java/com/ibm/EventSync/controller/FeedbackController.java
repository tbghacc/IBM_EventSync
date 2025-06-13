package com.ibm.EventSync.controller;

import com.ibm.EventSync.dto.FeedbackDTO;
import com.ibm.EventSync.model.Feedback;
import com.ibm.EventSync.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/events/{eventId}/feedback")
public class FeedbackController {

    private final FeedbackService feedbackService;

    @GetMapping("/{id}")
    public Feedback getFeedback(@PathVariable("eventId") UUID eventId, @PathVariable("id") UUID id) {
        return feedbackService.getFeedback(eventId, id);
    }

    @GetMapping
    public List<Feedback> getFeedbackByEvent(@PathVariable("eventId") UUID id) {
        return feedbackService.getFeedbackByEvent(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFeedback(@PathVariable("eventId") UUID eventId, @PathVariable("id") UUID id) {
        feedbackService.deleteFeedback(eventId, id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Feedback addFeedback(@PathVariable("eventId") UUID id, @Valid @RequestBody FeedbackDTO feedback) {
        return feedbackService.addFeedback(id, feedback);
    }

    // Incase setting the sentiment fails for some reason, you can call this to try again
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{id}")
    public void updateSentiment(@PathVariable("id") UUID id) {
        feedbackService.setSentiment(id, "");
    }
}

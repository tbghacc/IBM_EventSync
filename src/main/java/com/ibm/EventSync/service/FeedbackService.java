package com.ibm.EventSync.service;

import com.ibm.EventSync.dto.FeedbackDTO;
import com.ibm.EventSync.model.Feedback;
import com.ibm.EventSync.repository.FeedbackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final String HF_URL = "https://router.huggingface.co/hf-inference/models/cardiffnlp/twitter-roberta-base-sentiment";
    private final FeedbackRepository feedbackRepository;
    @Value("${HF_TOKEN}")
    private String apiKey;

    public Feedback getFeedback(UUID id) {
        return feedbackRepository.getFeedback(id);
    }

    public List<Feedback> getFeedbackByEvent(UUID eventid) {
        return feedbackRepository.getFeedbackByEvent(eventid);
    }

    // In a perfect world, setSentiment is run async to this, but should be fine for this demo
    public Feedback addFeedback(UUID id, FeedbackDTO feedbackDto) {
        Feedback feedback = Feedback.builder()
                .id(UUID.randomUUID())
                .event(id)
                .submitter(feedbackDto.getSubmitter())
                .feedback(feedbackDto.getFeedback())
                .build();
        feedbackRepository.insertFeedback(feedback);
        setSentiment(feedback.getId(), feedback.getFeedback());
        return feedback;
    }


    // Labels: 0 -> Negative; 1 -> Neutral; 2 -> Positive
    public void setSentiment(UUID id, String feedback) {
        /*
        Map<String, Double> sentiment = new HashMap<>();
        sentiment.put("LABEL_2", 0.0027578703593462706);
        sentiment.put("LABEL_1", 0.018482660874724388);
        sentiment.put("LABEL_0", 0.9787594676017761);
        */
        Map<String, Double> sentiment = getSentiment(feedback);
        feedbackRepository.setSentiment(
                id,
                sentiment.get("LABEL_2"),
                sentiment.get("LABEL_1"),
                sentiment.get("LABEL_0")
        );

    }

    public Map<String, Double> getSentiment(String feedback) {

        WebClient webClient = WebClient.builder()
                .baseUrl(HF_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return webClient.post()
                .headers(h -> h.setBearerAuth(apiKey))
                .bodyValue(Collections.singletonMap("inputs", feedback))
                .retrieve()
                .bodyToMono(new org.springframework.core.ParameterizedTypeReference<List<List<Map<String, Object>>>>() {
                })
                .map(response -> {
                    Map<String, Double> sentiment = new HashMap<>();
                    if (response != null && !response.isEmpty() && response.get(0) != null) {
                        for (Map<String, Object> score : response.get(0)) {
                            String label = (String) score.get("label");
                            Double value = (Double) score.get("score");
                            if (label != null && value != null) {
                                sentiment.put(label, value);
                            }
                        }
                    }
                    return sentiment;
                })
                .block();  // Blocking call for simplicity (e.g., in a simple test or script)
    }


}

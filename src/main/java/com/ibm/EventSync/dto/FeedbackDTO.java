package com.ibm.EventSync.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class FeedbackDTO {

    @Size(max = 20, message = "Name too long. Max 20 characters")
    String submitter;

    @NotBlank(message = "Feedback cannot be blank")
    @Size(max = 500, message = "Feedback too long. Max 500 characters")
    String feedback;

    Instant submitted;

}

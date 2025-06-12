package com.ibm.EventSync.dto;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class FeedbackDTO {

    String submitter;
    String feedback;
    Instant submitted;

}

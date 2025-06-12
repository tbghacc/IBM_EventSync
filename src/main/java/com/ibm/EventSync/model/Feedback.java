package com.ibm.EventSync.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.UUID;


@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Feedback {

    @Id
    private UUID id;
    private UUID event;
    private String submitter;
    private String feedback;
    private Double positive;
    private Double neutral;
    private Double negative;
    private Instant submitted;

}

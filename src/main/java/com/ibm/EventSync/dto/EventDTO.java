package com.ibm.EventSync.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EventDTO {

    @NotBlank
    String title;

    String description;
}

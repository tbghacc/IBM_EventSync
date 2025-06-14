package com.ibm.EventSync.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class EventDTO {

    @NotBlank(message = "Title cannot be blank")
    @Size(max = 50, message = "Title too long. Max 50 characters")
    String title;

    @Size(max = 200, message = "Description too long. Max 200 characters")
    String description;
}

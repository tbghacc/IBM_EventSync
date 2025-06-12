package com.ibm.EventSync.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Sentiment {

    private Double positive;
    private Double neutral;
    private Double negative;

}

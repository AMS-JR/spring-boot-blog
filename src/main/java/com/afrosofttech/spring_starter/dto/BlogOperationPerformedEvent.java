package com.afrosofttech.spring_starter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlogOperationPerformedEvent {

    private String operationType;
    private String userId;
    private String details;
    private LocalDateTime timestamp;

}

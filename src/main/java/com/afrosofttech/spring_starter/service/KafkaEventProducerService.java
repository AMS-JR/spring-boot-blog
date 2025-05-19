package com.afrosofttech.spring_starter.service;

import com.afrosofttech.spring_starter.dto.BlogOperationPerformedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaEventProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;


    public void publishBlogOperationsPerformedEvent(BlogOperationPerformedEvent event) {
        try {
            String eventJson = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("blog-operation-performed-topic", eventJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize blog operation performed event", e);
        }
    }

}


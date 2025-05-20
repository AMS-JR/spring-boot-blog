package com.afrosofttech.spring_starter.aop;

import com.afrosofttech.spring_starter.repository.PostRepository;
import com.afrosofttech.spring_starter.service.KafkaEventProducerService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class PostAspect {
    private final KafkaEventProducerService kafkaEventProducerService;
    private final PostRepository postRepository;

}

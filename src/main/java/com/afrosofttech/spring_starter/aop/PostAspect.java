package com.afrosofttech.spring_starter.aop;

import com.afrosofttech.spring_starter.dto.BlogOperationPerformedEvent;
import com.afrosofttech.spring_starter.entity.Post;
import com.afrosofttech.spring_starter.repository.PostRepository;
import com.afrosofttech.spring_starter.service.KafkaEventProducerService;
import com.afrosofttech.spring_starter.util.constants.OperationType;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
public class PostAspect {

    private final KafkaEventProducerService kafkaEventProducerService;
    private final PostRepository postRepository;

    private static final ThreadLocal<Boolean> isUpdate = new ThreadLocal<>();

    @Before("execution(* com.afrosofttech.spring_starter.service.impl.PostServiceImpl.save(..))")
    public void beforePostSave(JoinPoint joinPoint) {
        Post post = (Post) joinPoint.getArgs()[0];
        isUpdate.set(post.getId() != null); // If ID exists, it's an update
    }

    @AfterReturning(
            pointcut = "execution(* com.afrosofttech.spring_starter.service.impl.PostServiceImpl.save(..))",
            returning = "savedPost"
    )
    public void afterPostSave(JoinPoint joinPoint, Object savedPost) {
        if (!(savedPost instanceof Post post)) return;

        String operationType = Boolean.TRUE.equals(isUpdate.get())
                ? OperationType.POST_UPDATED.name()
                : OperationType.POST_CREATED.name();

        BlogOperationPerformedEvent event = BlogOperationPerformedEvent.builder()
                .operationType(operationType)
                .actor(post.getAccount() != null ? post.getAccount().getEmail() : "UNKNOWN")
                .timestamp(LocalDateTime.now())
                .details("Post saved via PostAspect (AOP)")
                .build();

        kafkaEventProducerService.publishBlogOperationsPerformedEvent(event);
        isUpdate.remove();
    }

    @AfterReturning("execution(* com.afrosofttech.spring_starter.service.impl.PostServiceImpl.delete(..))")
    public void afterPostDelete(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length == 0 || !(args[0] instanceof Post post)) return;

        BlogOperationPerformedEvent event = BlogOperationPerformedEvent.builder()
                .operationType(OperationType.POST_DELETED.name())
                .actor(post.getAccount() != null ? post.getAccount().getEmail() : "UNKNOWN")
                .timestamp(LocalDateTime.now())
                .details("Post deleted via PostAspect (AOP)")
                .build();

        kafkaEventProducerService.publishBlogOperationsPerformedEvent(event);
    }
}

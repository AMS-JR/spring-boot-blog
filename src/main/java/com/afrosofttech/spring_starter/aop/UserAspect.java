package com.afrosofttech.spring_starter.aop;

import com.afrosofttech.spring_starter.dto.BlogOperationPerformedEvent;
import com.afrosofttech.spring_starter.dto.EmailDto;
import com.afrosofttech.spring_starter.entity.Account;
import com.afrosofttech.spring_starter.repository.AccountRepository;
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
public class UserAspect {
    private final KafkaEventProducerService kafkaEventProducerService;
    private final AccountRepository accountRepository;

    private static final ThreadLocal<Boolean> isUpdate = new ThreadLocal<>();

    @Before("execution(* com.afrosofttech.spring_starter.service.impl.AccountServiceImpl.save(..))")
    public void beforeSave(JoinPoint joinPoint) {
        Account account = (Account) joinPoint.getArgs()[0];
        boolean exists = account.getId() != null && accountRepository.existsById(account.getId());
        isUpdate.set(exists);
    }

    @AfterReturning(
            pointcut = "execution(* com.afrosofttech.spring_starter.service.impl.AccountServiceImpl.save(..))",
            returning = "savedAccount"
    )
    public void afterSave(JoinPoint joinPoint, Object savedAccount) {
        if (!(savedAccount instanceof Account)) return;
        Account account = (Account) savedAccount;

        String operationType = Boolean.TRUE.equals(isUpdate.get())
                ? OperationType.USER_UPDATED.name()
                : OperationType.USER_CREATED.name();

        BlogOperationPerformedEvent event = BlogOperationPerformedEvent.builder()
                .operationType(operationType)
                .actor(account.getEmail())
                .timestamp(LocalDateTime.now())
                .details("From UserAspect (AOP)").build();

        kafkaEventProducerService.publishBlogOperationsPerformedEvent(event);

        isUpdate.remove(); // Clear ThreadLocal to prevent leaks
    }

    // After password is updated
    @AfterReturning("execution(* com.afrosofttech.spring_starter.service.impl.AccountServiceImpl.updatePassword(..))")
    public void afterPasswordUpdate(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args.length == 0 || !(args[0] instanceof Account)) return;

        Account account = (Account) args[0];

        BlogOperationPerformedEvent event = BlogOperationPerformedEvent.builder()
                .operationType(OperationType.USER_PASSWORD_CHANGED.name())
                .actor(accountRepository.findById(account.getId()).get().getEmail())
                .timestamp(LocalDateTime.now())
                .details("Password updated via AOP").build();

        kafkaEventProducerService.publishBlogOperationsPerformedEvent(event);
    }

    // After email is sent successfully
    @AfterReturning(
            pointcut = "execution(* com.afrosofttech.spring_starter.service.impl.EmailServiceImpl.sendSimpleMail(..))",
            returning = "result"
    )
    public void afterEmailSent(JoinPoint joinPoint, Object result) {
        if (!(result instanceof Boolean) || !(Boolean) result) return;

        Object[] args = joinPoint.getArgs();
        if (args.length == 0) return;

        String recipient = null;
        if (args[0] instanceof EmailDto emailDto) {
            recipient = emailDto.getRecipient();
        }

        BlogOperationPerformedEvent event = BlogOperationPerformedEvent.builder()
                .operationType(OperationType.EMAIL_SENT.name())
                .actor(recipient != null ? recipient : "UNKNOWN")
                .timestamp(LocalDateTime.now())
                .details("Email sent successfully via AOP").build();

        kafkaEventProducerService.publishBlogOperationsPerformedEvent(event);
    }

}

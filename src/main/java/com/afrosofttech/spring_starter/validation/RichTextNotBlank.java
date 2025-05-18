package com.afrosofttech.spring_starter.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = RichTextNotBlankValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RichTextNotBlank {
    String message() default "Body must not be blank";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

package com.afrosofttech.spring_starter.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RichTextNotBlankValidator implements ConstraintValidator<RichTextNotBlank, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;

        // Remove all HTML tags and check if there's meaningful content
        String textOnly = value.replaceAll("<[^>]*>", "").trim();
        return !textOnly.isEmpty();
    }
}

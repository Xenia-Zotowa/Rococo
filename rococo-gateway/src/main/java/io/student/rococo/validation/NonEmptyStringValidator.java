package io.student.rococo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NonEmptyStringValidator implements ConstraintValidator<NonEmptyString, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;
        return !value.trim().isEmpty();
    }
}
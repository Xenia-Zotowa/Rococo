package io.student.rococo.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = MuseumIdMustExistValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface MuseumIdMustExist {
    String message() default "Museum with specified ID must exist";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
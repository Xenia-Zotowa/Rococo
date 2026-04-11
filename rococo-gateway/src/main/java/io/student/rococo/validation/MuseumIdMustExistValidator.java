package io.student.rococo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.student.rococo.data.repository.MuseumRepository;
import java.util.UUID;

@Component
public class MuseumIdMustExistValidator implements ConstraintValidator<MuseumIdMustExist, String> {
    @Autowired
    private MuseumRepository museumRepository;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) return true;
        try {
            return museumRepository.existsById(UUID.fromString(value));
        } catch (Exception e) {
            return false;
        }
    }
}
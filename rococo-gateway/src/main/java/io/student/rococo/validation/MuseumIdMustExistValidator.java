package io.student.rococo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MuseumIdMustExistValidator implements ConstraintValidator<MuseumIdMustExist, Long> {
    @Autowired
    private io.student.rococo.data.repository.PaintingRepository paintingRepository;

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value == null) return true;
        try {
            return paintingRepository.existsById(value.longValue());
        } catch (Exception e) {
            return false;
        }
    }
}
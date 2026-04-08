package io.student.rococo.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import io.student.rococo.dto.GeoDTO;

public class GeoValidator implements ConstraintValidator<ValidGeo, GeoDTO> {
    @Override
    public boolean isValid(GeoDTO value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        if (value.getLatitude() == null || value.getLongitude() == null) {
            return false;
        }
        return value.getLatitude() >= -90 && value.getLatitude() <= 90 &&
                value.getLongitude() >= -180 && value.getLongitude() <= 180;
    }
}
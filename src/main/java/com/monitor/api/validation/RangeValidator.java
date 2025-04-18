package com.monitor.api.validation;

import com.monitor.api.entity.dto.SensorDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator class for validating the range values in a {@link SensorDto}.
 * Ensures that the {@code rangeFrom} value is less than the {@code rangeTo} value.
 * This validation is used in conjunction with the {@link ValidRange} annotation.
 */
public class RangeValidator implements ConstraintValidator<ValidRange, SensorDto> {

    /**
     * Validates if the {@code rangeFrom} value is less than the {@code rangeTo} value in a {@link SensorDto}.
     *
     * @param dto the {@link SensorDto} object to validate
     * @param context the context in which the constraint is evaluated (not used in this implementation)
     * @return {@code true} if the range values are valid (i.e., {@code rangeFrom} < {@code rangeTo}),
     *         {@code false} otherwise
     */
    @Override
    public boolean isValid(SensorDto dto, ConstraintValidatorContext context) {
        if (dto.getRangeFrom() == null || dto.getRangeTo() == null) {
            return true; // If any range is null, validation is skipped
        }
        return dto.getRangeFrom() < dto.getRangeTo();
    }
}

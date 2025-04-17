package com.monitor.api.validation;

import com.monitor.api.entity.dto.SensorDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RangeValidator implements ConstraintValidator<ValidRange, SensorDto> {

    @Override
    public boolean isValid(SensorDto dto, ConstraintValidatorContext context) {
        if (dto.getRangeFrom() == null || dto.getRangeTo() == null) {
            return true;
        }
        return dto.getRangeFrom() < dto.getRangeTo();
    }
}
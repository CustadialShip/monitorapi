package com.monitor.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RangeValidator.class)
@Documented
public @interface ValidRange {
    String message() default "rangeFrom must be less than rangeTo";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

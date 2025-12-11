package com.example.pancharm.validator.implementation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import com.example.pancharm.validator.annotation.UnitPriceConstraint;

public class UnitPriceValidator implements ConstraintValidator<UnitPriceConstraint, Integer> {
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        try {
            if (value == null) {
                return false;
            }
            return value >= 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void initialize(UnitPriceConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }
}

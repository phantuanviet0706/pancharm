package com.example.pancharm.validator.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import com.example.pancharm.validator.implementation.CategoryValidator;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CategoryValidator.class)
public @interface CategoryConstraint {
    String message() default "Category is invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

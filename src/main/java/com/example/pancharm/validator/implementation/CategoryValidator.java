package com.example.pancharm.validator.implementation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.pancharm.repository.CategoryRepository;
import com.example.pancharm.validator.annotation.CategoryConstraint;

public class CategoryValidator implements ConstraintValidator<CategoryConstraint, Integer> {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public void initialize(CategoryConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer categoryId, ConstraintValidatorContext constraintValidatorContext) {
        if (categoryId == null) {
            return false;
        }
        return categoryRepository.existsById(String.valueOf((categoryId)));
    }
}

package com.foodsquad.FoodSquad.validation.validators;

import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import com.foodsquad.FoodSquad.validation.annotations.NotEmptyLocalizedString;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LocalizedStringValidator implements ConstraintValidator<NotEmptyLocalizedString, LocalizedString> {

    @Override
    public boolean isValid(LocalizedString value, ConstraintValidatorContext context) {
        if (value == null || value.getTranslations() == null) {
            return false;
        }
        return value.getTranslations().values().stream().anyMatch(text -> text != null && !text.isBlank());
    }
}

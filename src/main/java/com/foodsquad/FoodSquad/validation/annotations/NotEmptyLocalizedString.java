package com.foodsquad.FoodSquad.validation.annotations;

import com.foodsquad.FoodSquad.validation.validators.LocalizedStringValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LocalizedStringValidator.class)
@Documented
public @interface NotEmptyLocalizedString {
    String message() default "LocalizedString cannot be empty";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

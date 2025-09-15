package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.config.context.LocaleContext;
import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Map;

/**
 * Utility mapper for converting between {@link LocalizedString} and plain {@link String}
 * values for the current locale.
 */
@Component
@RequiredArgsConstructor
public class LocalizedStringMapper {

    private final LocaleContext localeContext;

    /**
     * Extracts the translation for the current locale from a {@link LocalizedString}.
     *
     * @param localized the {@link LocalizedString} object, may be {@code null}
     * @return the translation in the current locale, or {@code null} if not found
     */
    public String toString(LocalizedString localized) {
        if (localized == null || localized.getTranslations() == null) {
            return null;
        }

        String locale = null;
        try {
            locale = getCurrentLocale();
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
        return localized.getTranslations().get(locale);
    }

    /**
     * Creates a {@link LocalizedString} with a single translation for the current locale.
     *
     * @param value the translation text, may be {@code null}
     * @return a {@link LocalizedString} with the current locale translation, or {@code null} if value is null
     */
    public LocalizedString toLocalized(String value) {
        if (value == null) {
            return null;
        }

        String locale = null;
        try {
            locale = getCurrentLocale();
        } catch (BadRequestException e) {
            throw new RuntimeException(e);
        }
        Map<String, String> translations = Collections.singletonMap(locale, value);

        return LocalizedString.builder()
                .translations(translations)
                .build();
    }

    /**
     * Gets the current locale from the context and ensures it is not empty.
     *
     * @return the current locale code
     * @throws BadRequestException if locale is missing or empty
     */
    private String getCurrentLocale() throws  BadRequestException {
        String locale = localeContext.getLocale();
        if (!StringUtils.hasText(locale)) {
            throw new BadRequestException("Locale must exist in the context");
        }
        return locale;
    }
}

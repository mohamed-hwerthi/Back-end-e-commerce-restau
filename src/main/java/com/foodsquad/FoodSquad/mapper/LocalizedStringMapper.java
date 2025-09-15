package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Utility mapper for converting between {@link LocalizedString} and plain {@link String}
 * values for a given locale.
 *
 * <p>This helper centralizes logic for:
 * <ul>
 *   <li>Extracting a translation in a specific locale from a {@link LocalizedString} object</li>
 *   <li>Creating a {@link LocalizedString} object from a plain {@link String} for a specific locale</li>
 * </ul>
 *
 * <p>Intended for reuse in multiple MapStruct mappers by declaring this class in
 * the {@code uses} attribute of the {@link org.mapstruct.Mapper} annotation.
 */
@Component
public class LocalizedStringMapper {

    /**
     * Extracts the translation for the given locale from a {@link LocalizedString}.
     *
     * @param localized the {@link LocalizedString} object, may be {@code null}
     * @param locale    the locale code to extract the translation for (e.g., "en", "fr")
     * @return the translation in the specified locale, or {@code null} if not found
     */
    public String toString(LocalizedString localized, String locale) {
        if (localized == null || localized.getTranslations() == null) {
            return null;
        }
        return localized.getTranslations().get(locale);
    }

    /**
     * Creates a {@link LocalizedString} object with a single translation for the given locale.
     *
     * @param value  the translation text, may be {@code null}
     * @param locale the locale code for the translation (e.g., "en", "fr")
     * @return a {@link LocalizedString} with the specified translation, or {@code null} if {@code value} is null
     */
    public LocalizedString toLocalized(String value, String locale) {
        if (value == null) {
            return null;
        }
        Map<String, String> translations = Map.of(locale, value);
        return LocalizedString.builder()
                .translations(translations)
                .build();
    }
}

package com.foodsquad.FoodSquad.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import static com.foodsquad.FoodSquad.config.utils.Constant.EN_LOCALE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalizedString {

    private Map<String, String> translations = new HashMap<>();

    public void addTranslation(String lang, String text) {
        translations.put(lang, text);
    }

    public String get(String lang) {
        return translations.getOrDefault(lang, translations.get(EN_LOCALE));
    }
}

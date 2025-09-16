    package com.foodsquad.FoodSquad.model.entity;

    import lombok.AllArgsConstructor;
    import lombok.Builder;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import java.util.HashMap;
    import java.util.Map;

    import static com.foodsquad.FoodSquad.config.utils.Constant.EN_LOCALE;

    import com.fasterxml.jackson.annotation.JsonAnyGetter;
    import com.fasterxml.jackson.annotation.JsonAnySetter;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public class LocalizedString {

        private Map<String, String> translations = new HashMap<>();

        @JsonAnySetter
        public void addTranslation(String lang, String text) {
            translations.put(lang, text);
        }

        @JsonAnyGetter
        public Map<String, String> getTranslations() {
            return translations;
        }

        public String get(String lang) {
            return translations.getOrDefault(lang, translations.get(EN_LOCALE));
        }
    }

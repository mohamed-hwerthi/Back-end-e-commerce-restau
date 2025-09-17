package com.foodsquad.FoodSquad.config.mapper;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodsquad.FoodSquad.model.entity.LocalizedString;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Map;

@Converter(autoApply = false)
public class LocalizedStringConverter implements AttributeConverter<LocalizedString, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(LocalizedString attribute) {
        if (attribute == null) return null;
        try {
            return objectMapper.writeValueAsString(attribute.getTranslations());
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert LocalizedString to JSON", e);
        }
    }

    @Override
    public LocalizedString convertToEntityAttribute(String dbData) {
        if (dbData == null) return new LocalizedString();
        try {
            return new LocalizedString(objectMapper.readValue(dbData, Map.class));
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert JSON to LocalizedString", e);
        }
    }
}


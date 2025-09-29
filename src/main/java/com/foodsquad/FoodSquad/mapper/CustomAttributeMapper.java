package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.CustomAttributeDTO;
import com.foodsquad.FoodSquad.model.entity.CustomAttribute;
import com.foodsquad.FoodSquad.model.entity.CustomAttributeType;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomAttributeMapper {

    @Mapping(target = "product", ignore = true)
    CustomAttribute toEntity(CustomAttributeDTO dto);

    CustomAttributeDTO toDto(CustomAttribute entity);

    List<CustomAttribute> toEntityList(List<CustomAttributeDTO> dtoList);

    List<CustomAttributeDTO> toDtoList(List<CustomAttribute> entityList);



    @Mapping(target = "id" , ignore = true)
    void updatePartialFields(@MappingTarget CustomAttribute customAttribute , CustomAttributeDTO  customAttributeDTO) ;

    /**
     * Convert value according to type
     */
    default String convertValueByType(String value, CustomAttributeType type) {
        if (value == null) return null;

        try {
            return switch (type) {
                case STRING -> value;
                case NUMBER -> {
                    Double.parseDouble(value);
                    yield value;
                }
                case DECIMAL -> {
                    new java.math.BigDecimal(value);
                    yield value;
                }
                case BOOLEAN -> {
                    if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
                        throw new IllegalArgumentException("Invalid boolean value");
                    }
                    yield value.toLowerCase();
                }
                case DATE -> {
                    java.time.LocalDate.parse(value);
                    yield value;
                }
                default -> throw new IllegalArgumentException("Unknown type: " + type);
            };
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid value for type " + type + ": " + value, e);
        }
    }


    /**
     * After mapping hook to apply value conversion
     */
    @AfterMapping
    default void applyValueConversion(@MappingTarget CustomAttribute entity, CustomAttributeDTO dto) {
        if (dto != null && dto.getType() != null) {
            entity.setValue(convertValueByType(dto.getValue(), dto.getType()));
        }
    }
}

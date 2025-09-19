package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.AttributeTypeDTO;
import com.foodsquad.FoodSquad.model.dto.AttributeValueDTO;
import com.foodsquad.FoodSquad.model.entity.AttributeType;
import com.foodsquad.FoodSquad.model.entity.AttributeValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AttributeValueMapper {

    AttributeValueMapper INSTANCE = Mappers.getMapper(AttributeValueMapper.class);

    @Mapping(source = "attributeType.id", target = "attributeTypeId")
    @Mapping(source = "attributeType", target = "attributeType", qualifiedByName = "mapAttributeTypeToDto")
    AttributeValueDTO toDto(AttributeValue attributeValue);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "attributeType", ignore = true)
    @Mapping(source = "attributeTypeId", target = "attributeType.id")
    @Mapping(target = "variantAttributes", ignore = true)
    AttributeValue toEntity(AttributeValueDTO dto);

    @Named("mapAttributeTypeToDto")
    default AttributeTypeDTO mapAttributeTypeToDto(AttributeType attributeType) {
        if (attributeType == null) {
            return null;
        }
        AttributeTypeDTO dto = new AttributeTypeDTO();
        dto.setId(attributeType.getId());
        dto.setName(attributeType.getName());
        return dto;
    }
}

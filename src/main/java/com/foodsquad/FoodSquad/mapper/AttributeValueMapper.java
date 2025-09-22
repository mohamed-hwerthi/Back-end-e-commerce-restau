package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.AttributeTypeDTO;
import com.foodsquad.FoodSquad.model.dto.AttributeValueDTO;
import com.foodsquad.FoodSquad.model.entity.AttributeType;
import com.foodsquad.FoodSquad.model.entity.ProductAttributeValue;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AttributeValueMapper {

    AttributeValueMapper INSTANCE = Mappers.getMapper(AttributeValueMapper.class);


    AttributeValueDTO toDto(ProductAttributeValue attributeValue);


    ProductAttributeValue toEntity(AttributeValueDTO dto);

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

package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.AttributeTypeDTO;
import com.foodsquad.FoodSquad.model.dto.ProductAttributeValueDTO;
import com.foodsquad.FoodSquad.model.entity.AttributeType;
import com.foodsquad.FoodSquad.model.entity.ProductAttributeValue;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AttributeValueMapper {

    AttributeValueMapper INSTANCE = Mappers.getMapper(AttributeValueMapper.class);


    ProductAttributeValueDTO toDto(ProductAttributeValue attributeValue);


    ProductAttributeValue toEntity(ProductAttributeValueDTO dto);

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

package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.dto.ProductOptionDTO;
import com.foodsquad.FoodSquad.model.entity.ProductOption;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductOptionMapper {
    @Mapping(target = "linkedProduct.id"  ,source = "linkedProductId")
    ProductOption toEntity(ProductOptionDTO dto);
    @Mapping(target = "linkedProductId"  ,source = "linkedProduct.id")
    ProductOptionDTO toDto(ProductOption entity);

    List<ProductOption> toEntityList(List<ProductOptionDTO> dtos);

    List<ProductOptionDTO> toDtoList(List<ProductOption> entities);
}

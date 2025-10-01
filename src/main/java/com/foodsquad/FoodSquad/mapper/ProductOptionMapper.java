package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.dto.ProductOptionDTO;
import com.foodsquad.FoodSquad.model.entity.ProductOption;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductOptionMapper {

    ProductOption toEntity(ProductOptionDTO dto);

    ProductOptionDTO toDto(ProductOption entity);

    List<ProductOption> toEntityList(List<ProductOptionDTO> dtos);

    List<ProductOptionDTO> toDtoList(List<ProductOption> entities);
}

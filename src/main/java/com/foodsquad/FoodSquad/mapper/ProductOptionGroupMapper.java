package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.dto.ProductOptionGroupDTO;
import com.foodsquad.FoodSquad.model.entity.ProductOptionGroup;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring"  , uses = {ProductOptionMapper.class})
public interface ProductOptionGroupMapper {

    ProductOptionGroup toEntity(ProductOptionGroupDTO dto);

    ProductOptionGroupDTO toDto(ProductOptionGroup entity);

    List<ProductOptionGroup> toEntityList(List<ProductOptionGroupDTO> dtos);

    List<ProductOptionGroupDTO> toDtoList(List<ProductOptionGroup> entities);
}

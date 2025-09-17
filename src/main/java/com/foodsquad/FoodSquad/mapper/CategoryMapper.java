package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.CategoryDTO;
import com.foodsquad.FoodSquad.model.entity.Category;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {MediaMapper.class})
public interface CategoryMapper {

    Category toEntity(CategoryDTO categoryDTO);

    CategoryDTO toDto(Category category);

    List<CategoryDTO> toDTOList(List<Category> categories);

    @Mapping(target = "id", ignore = true)
    void updateExistedCategoryFromDTO(CategoryDTO categoryDTO, @MappingTarget Category category);

}

package com.foodsquad.FoodSquad.mapper;


import com.foodsquad.FoodSquad.model.dto.ProductDTO;
import com.foodsquad.FoodSquad.model.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {CategoryMapper.class, MediaMapper.class, TaxMapper.class})

public interface ProductMapper {

    Product toEntity(ProductDTO productDTO);

    ProductDTO toDto(Product product);

    List<ProductDTO> toDtoList(List<Product> products);

    @Mapping(target = "id", ignore = true)
    void updateProductFromDto(ProductDTO dto, @MappingTarget Product entity);

    default ProductDTO toProductDtoWithMoreInformation(Product product, int salesCount, long reviewCount, double averageRating) {

        ProductDTO productDTO = toDto(product);
        productDTO.setSalesCount(salesCount);
        productDTO.setReviewCount(reviewCount);
        productDTO.setAverageRating(averageRating);


        return productDTO;
    }

}

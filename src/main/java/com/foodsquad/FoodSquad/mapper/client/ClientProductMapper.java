package com.foodsquad.FoodSquad.mapper.client;

import com.foodsquad.FoodSquad.config.context.LocaleContext;
import com.foodsquad.FoodSquad.mapper.GenericMapper;
import com.foodsquad.FoodSquad.model.dto.client.ClientProductDTO;
import com.foodsquad.FoodSquad.model.dto.client.ClientProductVariantOption;
import com.foodsquad.FoodSquad.model.dto.client.ClientProductVariants;
import com.foodsquad.FoodSquad.model.entity.Media;
import com.foodsquad.FoodSquad.model.entity.Product;
import com.foodsquad.FoodSquad.model.entity.ProductAttribute;
import com.foodsquad.FoodSquad.model.entity.ProductAttributeValue;
import org.mapstruct.*;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {ClientProductOptionGroupMapper.class})
public interface ClientProductMapper extends GenericMapper<Product, ClientProductDTO> {

    @Mapping(target = "title", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "variants", ignore = true)
    @Mapping(target = "categoryName", ignore = true)
    @Mapping(target = "mediasUrls", ignore = true)
    @Mapping(target = "basePrice", source = "price")
    @Mapping(target = "optionGroups", source = "productOptionGroups")
    @Override
    ClientProductDTO toDto(Product product);

    @Mapping(target = "title", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Override
    Product toEntity(ClientProductDTO dto);

    @Mapping(target = "title", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Override
    default void updateEntityFromDto(ClientProductDTO dto, Product entity) {

    }

    /**
     * Applies locale-based translation for the option name after mapping.
     */
        @AfterMapping
        default void afterMappingLocaleTranslation(Product entity, @MappingTarget ClientProductDTO dto) {
            String locale = LocaleContext.get();

            Optional.ofNullable(entity.getTitle())
                    .map(titleMap -> titleMap.get(locale))
                    .filter(name -> !ObjectUtils.isEmpty(name))
                    .ifPresent(dto::setTitle);
            Optional.ofNullable(entity.getDescription())
                    .map(descriptionMap -> descriptionMap.get(locale))
                    .filter(name -> !ObjectUtils.isEmpty(name))
                    .ifPresent(dto::setDescription);
            Optional.ofNullable(entity.getCategories())
                    .filter(categories -> !ObjectUtils.isEmpty(categories))
                    .map(categories -> categories.get(0).getName())
                    .filter(name -> !ObjectUtils.isEmpty(name))
                    .map(categoryMap -> categoryMap.get(locale))
                    .filter(name -> !ObjectUtils.isEmpty(name))
                    .ifPresent(dto::setCategoryName);

        }

    /**
     * Applies locale-based translation for the option name after mapping.
     */
    @AfterMapping
    default void afterMappingLocaleMediaUrls(Product entity, @MappingTarget ClientProductDTO dto) {
        if(!ObjectUtils.isEmpty(entity.getMedias())){
            dto.setMediasUrls(entity.getMedias().stream().map(Media::getUrl).toList());
        }


    }


    @AfterMapping
    default void enrichDetailWithVariantsAndOptions(Product product, @MappingTarget ClientProductDTO dto) {
        if (!ObjectUtils.isEmpty(product.getVariants())) {
            dto.setVariants(buildVariantsForClientProductDTO(product));

        }
    }

    private List<ClientProductVariants> buildVariantsForClientProductDTO(Product product) {

        Map<UUID, List<Product>> groupedByAttribute = product.getVariants().stream()
                .flatMap(variant -> variant.getVariantAttributes().stream()
                        .map(attrValue -> Map.entry(attrValue.getProductAttribute().getId(), variant))
                )
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                ));


        return groupedByAttribute.entrySet().stream()
                .map(entry -> {
                    UUID attributeId = entry.getKey();
                    List<Product> variantsForAttribute = entry.getValue();

                    ProductAttribute attribute = variantsForAttribute.get(0).getVariantAttributes().stream()
                            .filter(val -> val.getProductAttribute().getId().equals(attributeId))
                            .findFirst()
                            .map(ProductAttributeValue::getProductAttribute)
                            .orElseThrow();

                    List<ClientProductVariantOption> options = variantsForAttribute.stream()
                            .map(variant -> {
                                ProductAttributeValue attrValue = variant.getVariantAttributes().stream()
                                        .filter(val -> val.getProductAttribute().getId().equals(attributeId))
                                        .findFirst()
                                        .orElseThrow();


                                return ClientProductVariantOption.builder()
                                        .variantId(variant.getId())
                                        .variantValue(attrValue.getValue())
                                        .variantPrice(variant.getPrice())
                                        .build();
                            })
                            .toList();

                    return ClientProductVariants.builder()
                            .variantName(attribute.getName())
                            .options(options)
                            .build();
                })
                .toList();
    }


}

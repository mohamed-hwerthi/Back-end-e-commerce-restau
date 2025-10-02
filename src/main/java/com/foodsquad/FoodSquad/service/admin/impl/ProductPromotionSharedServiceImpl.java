package com.foodsquad.FoodSquad.service.admin.impl;

import com.foodsquad.FoodSquad.exception.ProductHasActivePromotionInAPeriodException;
import com.foodsquad.FoodSquad.mapper.ProductMapper;
import com.foodsquad.FoodSquad.mapper.PromotionMapper;
import com.foodsquad.FoodSquad.model.dto.ProductDTO;
import com.foodsquad.FoodSquad.model.dto.PromotionDTO;
import com.foodsquad.FoodSquad.model.dto.PromotionType;
import com.foodsquad.FoodSquad.model.entity.PercentageDiscountPromotion;
import com.foodsquad.FoodSquad.model.entity.Product;
import com.foodsquad.FoodSquad.model.entity.Promotion;
import com.foodsquad.FoodSquad.service.admin.dec.ProductPromotionSharedService;
import com.foodsquad.FoodSquad.service.admin.dec.ProductService;
import com.foodsquad.FoodSquad.service.admin.dec.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ProductPromotionSharedServiceImpl implements ProductPromotionSharedService {

    private final ProductService ProductService;

    private final PromotionMapper promotionMapper;

    private final PromotionService promotionService;

    private final ProductMapper productMapper;


    @Override
    public PromotionDTO createPromotionForProducts(List<UUID> ProductsIds, PromotionDTO promotionDTO) {

        List<ProductDTO> products = getProducts(ProductsIds);

        if (promotionDTO.getPromotionType().equals(PromotionType.DISCOUNT)) {
            return createPercentageDiscountPromotion(promotionDTO, products);

        }
        return null;
    }

    @Override
    public List<ProductDTO> findProductsRelatedToPromotion(UUID promotionId) {

        Promotion promotion = promotionService.getPromotion(promotionId);

        return productMapper.toDtoList(promotion.getProducts());
    }

    private PromotionDTO createPercentageDiscountPromotion(PromotionDTO promotionDTO, List<ProductDTO> products) {

        PercentageDiscountPromotion promotion = promotionMapper.mapPromotionDTOToPercentageDiscountPromotion(promotionDTO);

        Promotion savePromotion = promotionService.savePromotion(promotion);

        associatePromotionWithProducts(savePromotion, products);

        Promotion savedPromotion = promotionService.savePromotion(promotion);

        return promotionMapper.toDTO(savedPromotion);
    }


    @Override
    public boolean hasActivePromotionOverlappingPeriod(UUID ProductId, LocalDate startDate, LocalDate endDate) {
        Product product = ProductService.findProductById(ProductId);

        return product.getPromotions().stream()
                .filter(Promotion::isActive)
                .anyMatch(promotion ->
                        arePeriodsOverlapping(
                                promotion.getStartDate(),
                                promotion.getEndDate(),
                                startDate,
                                endDate
                        )
                );
    }

    @Override
    public void deactivatePromotionForProduct(UUID ProductId, UUID promotionId) {

        Product product = ProductService.findProductById(ProductId);

        product.getPromotions().removeIf(promotion -> promotion.getId().equals(promotionId));

        ProductService.save(product);

    }


    @Override
    public void addPromotionToProduct(UUID ProductId, UUID promotionId) {

        Product product = ProductService.findProductById(ProductId);

        Promotion promotion = promotionService.getPromotion(promotionId);

        if (hasActivePromotionOverlappingPeriod(ProductId, promotion.getStartDate(), promotion.getEndDate())) {
            throw new ProductHasActivePromotionInAPeriodException("Product has active promotion in this  period");

        }

        product.getPromotions().add(promotionService.getPromotion(promotionId));

        ProductService.save(product);

    }


    @Override
    public boolean isProductHasActivePromotionInCurrentDay(UUID ProductId) {
        List<PromotionDTO> promotionDTOS = promotionService.findAllPromotionForProduct(ProductId);
        LocalDate today = LocalDate.now();
        return promotionDTOS.stream()
                .filter(PromotionDTO::isActive)
                .anyMatch(promotionDTO ->
                        (!today.isBefore(promotionDTO.getStartDate())) &&
                                (!today.isAfter(promotionDTO.getEndDate()))
                );
    }



    /*
   todo  : we have to add some  logic here  for getting the  last created promotion  :
    our metier is to apply the last created  promotion for the  same type

     **  getting related categories promotions to display them
     */

    @Override
    public PercentageDiscountPromotion getProductActivePromotionInCurrentDay(UUID ProductId) {
        List<Promotion> ProductPromotions = promotionService.findPromotionsForProduct(ProductId);
        LocalDate today = LocalDate.now();

        return ProductPromotions.stream()
                .filter(Promotion::isActive)
                .filter(promotion ->
                        promotion.getStartDate() != null && promotion.getEndDate() != null &&
                                !today.isBefore(promotion.getStartDate()) && !today.isAfter(promotion.getEndDate())
                )
                .filter(promotion -> promotion instanceof PercentageDiscountPromotion)
                .map(promotion -> (PercentageDiscountPromotion) promotion).max((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()))
                .orElse(null);
    }


    private void associatePromotionWithProducts(Promotion promotion, List<ProductDTO> ProductDTOS) {

        List<Product> products = ProductDTOS.stream().map(
                productDTO -> {
                    Product product = productMapper.toEntity(productDTO);
                    product.getPromotions().add(promotion);
                    return product;
                }
        ).toList();


        ProductService.saveProducts(products);

    }


    /**
     * Vérifie si deux périodes se chevauchent (au moins un jour en commun).
     */
    private boolean arePeriodsOverlapping(LocalDate period1Start, LocalDate period1End, LocalDate period2Start, LocalDate period2End) {
        return !period1End.isBefore(period2Start) && !period1Start.isAfter(period2End);
    }

    private List<ProductDTO> getProducts(List<UUID> ProductsIds) {


        return ProductsIds.stream().map(ProductService::getProductById).toList();

    }


}

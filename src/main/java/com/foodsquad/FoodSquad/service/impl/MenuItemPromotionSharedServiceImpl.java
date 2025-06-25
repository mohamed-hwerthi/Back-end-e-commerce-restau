package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.exception.MenuItemHasActivePromotionInAPeriodException;
import com.foodsquad.FoodSquad.mapper.MenuItemMapper;
import com.foodsquad.FoodSquad.mapper.PromotionMapper;
import com.foodsquad.FoodSquad.model.dto.MenuItemDTO;
import com.foodsquad.FoodSquad.model.dto.PromotionDTO;
import com.foodsquad.FoodSquad.model.dto.PromotionType;
import com.foodsquad.FoodSquad.model.entity.MenuItem;
import com.foodsquad.FoodSquad.model.entity.PercentageDiscountPromotion;
import com.foodsquad.FoodSquad.model.entity.Promotion;
import com.foodsquad.FoodSquad.service.declaration.MenuItemPromotionSharedService;
import com.foodsquad.FoodSquad.service.declaration.MenuItemService;
import com.foodsquad.FoodSquad.service.declaration.PromotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;



@Service
@RequiredArgsConstructor
public class MenuItemPromotionSharedServiceImpl  implements MenuItemPromotionSharedService {

    private final MenuItemService menuItemService;

    private final PromotionMapper promotionMapper;

    private final PromotionService promotionService;

    private final MenuItemMapper menuItemMapper;



    @Override
    public PromotionDTO createPromotionForMenuItems(List<Long> menuItemsIds, PromotionDTO promotionDTO) {

        List<MenuItemDTO> menuItems = getMenuItems(menuItemsIds);

        if (promotionDTO.getPromotionType().equals(PromotionType.DISCOUNT)) {
            return createPercentageDiscountPromotion(promotionDTO, menuItems);

        }
        return null;


    }

    @Override
    public List<MenuItemDTO> findMenuItemsRelatedToPromotion(Long promotionId) {

        Promotion promotion = promotionService.getPromotion(promotionId) ;

        return menuItemMapper.toDtoList(promotion.getMenuItems());
    }

    private PromotionDTO createPercentageDiscountPromotion(PromotionDTO promotionDTO, List<MenuItemDTO> menuItems) {

        PercentageDiscountPromotion promotion = promotionMapper.mapPromotionDTOToPercentageDiscountPromotion(promotionDTO);

        Promotion savePromotion = promotionService.savePromotion(promotion);

        associatePromotionWithMenuItems(savePromotion, menuItems);
        Promotion   savedPromotion = promotionService.savePromotion(promotion);

        return promotionMapper.toDTO(savedPromotion);
    }


    @Override
    public boolean hasActivePromotionOverlappingPeriod(Long menuItemId, LocalDate startDate, LocalDate endDate) {
        MenuItem menuItem = menuItemService.findMenuItemById(menuItemId);

        return menuItem.getPromotions().stream()
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
    public void deactivatePromotionForMenuItem(Long menuItemId, Long promotionId) {

        MenuItem menuItem = menuItemService.findMenuItemById(menuItemId);

        menuItem.getPromotions().removeIf(promotion -> promotion.getId().equals(promotionId));

        menuItemService.save(menuItem);

    }


    @Override
    public void addPromotionToMenuItem(Long menuItemId, Long promotionId) {

        MenuItem menuItem = menuItemService.findMenuItemById(menuItemId);

        Promotion promotion = promotionService.getPromotion(promotionId);

        if (hasActivePromotionOverlappingPeriod(menuItemId, promotion.getStartDate(), promotion.getEndDate())) {
            throw  new MenuItemHasActivePromotionInAPeriodException("MenuItem has active promotion in this  period");

        }

        menuItem.getPromotions().add(promotionService.getPromotion(promotionId));

        menuItemService.save(menuItem) ;

    }

    private List<MenuItemDTO> getMenuItems(List<Long> menuItemsIds) {


        return menuItemsIds.stream().map(menuItemService::getMenuItemById).toList();

    }


    @Override
    public boolean isMenuItemHasActivePromotionInCurrentDay(Long menuItemId) {
        MenuItem menuItem = menuItemService.findMenuItemById(menuItemId);

        LocalDate today = LocalDate.now();

        return menuItem.getPromotions().stream()
                .filter(Promotion::isActive)
                .anyMatch(promotion ->
                        ( !today.isBefore(promotion.getStartDate()) ) &&
                                ( !today.isAfter(promotion.getEndDate()) )
                );
    }


    private void associatePromotionWithMenuItems(Promotion promotion, List<MenuItemDTO> menuItemDTOS) {

        List<MenuItem> menuItems = menuItemDTOS.stream().map(
                menuItemDTO -> {
                    MenuItem menuItem = menuItemMapper.toEntity(menuItemDTO);
                    menuItem.getPromotions().add(promotion);
                    return menuItem;
                }
        ).toList();


        menuItemService.saveMenuItems(menuItems);

    }

    @Override
    public PercentageDiscountPromotion getMenuItemActivePromotionInCurrentDay(Long menuItemId) {

        MenuItem menuItem = menuItemService.findMenuItemById(menuItemId);

        LocalDate today = LocalDate.now();

        return menuItem.getPromotions().stream()
                .filter(Promotion::isActive)
                .filter(promotion ->
                        ( !today.isBefore(promotion.getStartDate()) ) &&
                                ( !today.isAfter(promotion.getEndDate()) )

                )
                .filter(promotion -> promotion instanceof PercentageDiscountPromotion)
                .map(promotion -> (PercentageDiscountPromotion) promotion)


                .findFirst().orElse(null);
    }





    /**
     * Vérifie si deux périodes se chevauchent (au moins un jour en commun).
     */
    private boolean arePeriodsOverlapping(LocalDate period1Start, LocalDate period1End, LocalDate period2Start, LocalDate period2End) {
                return !period1End.isBefore(period2Start) && !period1Start.isAfter(period2End);
    }





}

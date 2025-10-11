package com.foodsquad.FoodSquad.mapper.client;

import com.foodsquad.FoodSquad.config.context.LocaleContext;
import com.foodsquad.FoodSquad.mapper.GenericMapper;
import com.foodsquad.FoodSquad.model.dto.client.ClientOrderDTO;
import com.foodsquad.FoodSquad.model.entity.Order;
import org.mapstruct.*;
import org.springframework.util.StringUtils;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClientOrderMapper extends GenericMapper<Order, ClientOrderDTO> {

    @Mapping(target = "status", ignore = true)
    @Override
    ClientOrderDTO toDto(Order order);

    @Override
    default Order toEntity(ClientOrderDTO dto) {
        return null;
    }

    @Override
    default void updateEntityFromDto(ClientOrderDTO dto, Order entity) {

    }

    /**
     * Localize order status name based on the current LocaleContext.
     */
    @AfterMapping
    default void applyLocalizedStatus(Order order, @MappingTarget ClientOrderDTO dto) {
        String locale = LocaleContext.get();

        if (order.getStatus() != null &&
                order.getStatus().getName() != null &&
                StringUtils.hasText(order.getStatus().getName().get(locale))) {

            dto.setStatus(order.getStatus().getName().get(locale));
        } else {
            dto.setStatus(null);
        }
    }
}

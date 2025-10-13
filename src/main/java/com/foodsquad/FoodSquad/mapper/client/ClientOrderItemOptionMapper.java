package com.foodsquad.FoodSquad.mapper.client;

import com.foodsquad.FoodSquad.config.context.LocaleContext;
import com.foodsquad.FoodSquad.mapper.GenericMapper;
import com.foodsquad.FoodSquad.model.dto.client.ClientOrderItemOptionDTO;
import com.foodsquad.FoodSquad.model.entity.OrderItemOption;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING )
public interface ClientOrderItemOptionMapper extends GenericMapper<OrderItemOption, ClientOrderItemOptionDTO> {

    @Override
    default OrderItemOption toEntity(ClientOrderItemOptionDTO dto) {
        return null;
    }

    @Override
    default ClientOrderItemOptionDTO toDto(OrderItemOption entity) {
        if(entity ==null){
            return null  ;
        }
        ClientOrderItemOptionDTO dto =  new ClientOrderItemOptionDTO() ;
        String locale = LocaleContext.get();
        Optional.ofNullable(entity.getProductOption().getId())
                .ifPresent(dto::setOptionId);

        Optional.ofNullable(entity.getProductOption().getLinkedProduct().getTitle())
                .map(titleMap -> titleMap.get(locale))
                .filter(name -> !ObjectUtils.isEmpty(name))
                .ifPresent(dto::setOptionName);
        Optional.ofNullable(entity.getProductOption().getOverridePrice())
                .ifPresent(dto::setOptionPrice);
        return dto  ;
    }

    @Override
    default void updateEntityFromDto(ClientOrderItemOptionDTO dto, OrderItemOption entity) {

    }



}

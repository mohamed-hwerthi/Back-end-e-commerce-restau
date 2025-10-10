package com.foodsquad.FoodSquad.mapper.client;


import com.foodsquad.FoodSquad.config.context.LocaleContext;
import com.foodsquad.FoodSquad.mapper.GenericMapper;
import com.foodsquad.FoodSquad.model.dto.client.ClientOrderDTO;
import com.foodsquad.FoodSquad.model.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING , uses = {})
public  interface ClientOrderMapper extends GenericMapper<Order, ClientOrderDTO> {


    @Mapping(target = "status" , ignore = true)
     ClientOrderDTO toClientOrderWithoutTranslatedFields(Order order  );

    default ClientOrderDTO toClientOrderDTO(Order order , LocaleContext localeContext){
        ClientOrderDTO clientOrderDTO =  toClientOrderWithoutTranslatedFields(order);
        if(!ObjectUtils.isEmpty(order.getStatus())){
              if(StringUtils.hasText( order.getStatus().getName().get(localeContext.getLocale()))){
                  clientOrderDTO.setStatus(order.getStatus().getName().get(localeContext.getLocale()));
              }

        }
        else{
            clientOrderDTO.setStatus(null);
        }
    }
}

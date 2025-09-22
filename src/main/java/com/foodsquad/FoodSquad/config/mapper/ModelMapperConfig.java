package com.foodsquad.FoodSquad.config.mapper;

import com.foodsquad.FoodSquad.model.dto.OrderDTO;
import com.foodsquad.FoodSquad.model.entity.Order;
import com.foodsquad.FoodSquad.model.entity.Product;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(new PropertyMap<Order, OrderDTO>() {
            @Override
            protected void configure() {
                using(ctx -> ((Map<Product, Integer>) ctx.getSource()).entrySet().stream()
                        .collect(Collectors.toMap(e -> e.getKey().getId(), Map.Entry::getValue)))
                        .map(source.getMenuItemsWithQuantity(), destination.getMenuItemQuantities());
            }
        });
        return modelMapper;
    }
}

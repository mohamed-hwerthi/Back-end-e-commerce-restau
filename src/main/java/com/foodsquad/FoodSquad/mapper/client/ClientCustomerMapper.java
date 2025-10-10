package com.foodsquad.FoodSquad.mapper.client;

import com.foodsquad.FoodSquad.mapper.GenericMapper;
import com.foodsquad.FoodSquad.model.dto.client.ClientCustomerDTO;
import com.foodsquad.FoodSquad.model.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClientCustomerMapper extends GenericMapper<Customer , ClientCustomerDTO> {
    @Mapping(target = "id", ignore = true)
    void updateExistedCustomerFromDTO(ClientCustomerDTO clientCustomerDTO, @MappingTarget Customer customer);
}

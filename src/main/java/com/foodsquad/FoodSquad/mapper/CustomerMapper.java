package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.CustomerDTO;
import com.foodsquad.FoodSquad.model.entity.Customer;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper extends GenericMapper<Customer, CustomerDTO> {

    @Mapping(target = "id", ignore = true)
    void updateExistedCustomerFromDTO(CustomerDTO customerDTO, @MappingTarget Customer customer);
}

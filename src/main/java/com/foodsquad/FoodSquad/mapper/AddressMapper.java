package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.AddressDTO;
import com.foodsquad.FoodSquad.model.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING ,  uses = {CountryMapper.class})
public interface AddressMapper extends GenericMapper<Address, AddressDTO> {

}

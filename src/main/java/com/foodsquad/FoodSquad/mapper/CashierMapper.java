package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.CashierDTO;
import com.foodsquad.FoodSquad.model.entity.Cashier;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, 
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CashierMapper {

    @Mapping(target = "id", ignore = true)
    Cashier toEntity(CashierDTO cashierDTO);
    
    CashierDTO toDto(Cashier cashier);
    
    @Mapping(target = "id", ignore = true)
    void updateCashierFromDto(CashierDTO cashierDTO, @MappingTarget Cashier cashier);
}

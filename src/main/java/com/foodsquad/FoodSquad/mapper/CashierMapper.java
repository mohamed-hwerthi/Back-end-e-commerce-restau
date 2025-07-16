package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.CashierDTO;
import com.foodsquad.FoodSquad.model.entity.Cashier;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CashierMapper {

    Cashier toEntity(CashierDTO cashierDTO);

    CashierDTO toDto(Cashier cashier);

    void updateCashierFromDto(CashierDTO cashierDTO, @MappingTarget Cashier cashier);
}

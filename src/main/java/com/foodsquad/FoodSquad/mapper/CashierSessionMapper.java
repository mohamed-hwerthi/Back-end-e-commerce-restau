package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.cashier.CashierSessionRequestDTO;
import com.foodsquad.FoodSquad.model.dto.cashier.CashierSessionResponseDTO;
import com.foodsquad.FoodSquad.model.entity.CashierSession;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring",
        uses = {UserMapper.class})
public interface CashierSessionMapper extends GenericMapper<CashierSession, CashierSessionResponseDTO> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sessionNumber", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "cashMovements", ignore = true)
    @Mapping(target = "cashier.id", source = "cashierId")
    CashierSession toEntity(CashierSessionRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sessionNumber", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "cashMovements", ignore = true)
    @Mapping(target = "cashier", ignore = true)
    void updateEntityFromDto(CashierSessionRequestDTO dto, @MappingTarget CashierSession entity);

    @Mapping(target = "cashierId", ignore = true)
    @Mapping(target = "cashierName", expression = "java(entity.getCashier().getFirstName() + ' ' + entity.getCashier().getLastName())")
    @Mapping(target = "orderIds", expression = "java(entity.getOrders() != null ? entity.getOrders().stream().map(order -> order.getId()).collect(java.util.stream.Collectors.toList()) : null)")
    @Mapping(target = "cashMovementIds", expression = "java(entity.getCashMovements() != null ? entity.getCashMovements().stream().map(movement -> movement.getId()).collect(java.util.stream.Collectors.toList()) : null)")
    @Override
    CashierSessionResponseDTO toDto(CashierSession entity);
}

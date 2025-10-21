package com.foodsquad.FoodSquad.mapper;

import com.foodsquad.FoodSquad.model.dto.cashmovement.CashMovementRequestDTO;
import com.foodsquad.FoodSquad.model.dto.cashmovement.CashMovementResponseDTO;
import com.foodsquad.FoodSquad.model.entity.CashMovement;
import com.foodsquad.FoodSquad.model.entity.CashierSession;
import com.foodsquad.FoodSquad.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CashMovementMapper {
    CashMovementMapper INSTANCE = Mappers.getMapper(CashMovementMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timestamp", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "cashierSession", source = "cashierSessionId", qualifiedByName = "toCashierSession")
    @Mapping(target = "cashier", source = "cashierId", qualifiedByName = "toUser")
    CashMovement toEntity(CashMovementRequestDTO dto);

    @Mapping(target = "cashierSessionId", source = "cashierSession.id")
    @Mapping(target = "cashierId", source = "cashier.id")
    @Mapping(target = "cashierName", source = "cashier", qualifiedByName = "getCashierName")
    CashMovementResponseDTO toDto(CashMovement entity);

    @Named("toCashierSession")
    default CashierSession toCashierSession(UUID id) {
        if (id == null) return null;
        return CashierSession.builder().id(id).build();
    }

    @Named("toUser")
    default User toUser(UUID id) {
        if (id == null) return null;
        return User.builder().id(id).build();
    }

    @Named("getCashierName")
    default String getCashierName(User user) {
        if (user == null) return null;
        return user.getFirstName() + " " + user.getLastName();
    }
}

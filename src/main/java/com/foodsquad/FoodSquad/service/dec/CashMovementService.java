package com.foodsquad.FoodSquad.service.dec;

import com.foodsquad.FoodSquad.model.dto.cashmovement.CashMovementRequestDTO;
import com.foodsquad.FoodSquad.model.dto.cashmovement.CashMovementResponseDTO;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CashMovementService {
    CashMovementResponseDTO createCashMovement(CashMovementRequestDTO requestDTO);

    CashMovementResponseDTO getCashMovementById(UUID id);

    List<CashMovementResponseDTO> getCashMovementsBySession(UUID sessionId);

    List<CashMovementResponseDTO> getCashMovementsByCashier(UUID cashierId);

    void deleteCashMovement(UUID id);

    PaginatedResponseDTO<CashMovementResponseDTO> getAllCashMovements(Pageable pageable);
}

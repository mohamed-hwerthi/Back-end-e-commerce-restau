package com.foodsquad.FoodSquad.service;

import com.foodsquad.FoodSquad.model.dto.cashier.CashierSessionRequestDTO;
import com.foodsquad.FoodSquad.model.dto.cashier.CashierSessionResponseDTO;
import com.foodsquad.FoodSquad.model.entity.CashierSession;

import java.util.List;
import java.util.UUID;

public interface CashierSessionService {
    CashierSessionResponseDTO createSession(CashierSessionRequestDTO requestDTO);

    CashierSessionResponseDTO updateSession(UUID id, CashierSessionRequestDTO requestDTO);

    CashierSessionResponseDTO closeSession(UUID id, Double closingBalance);

    CashierSessionResponseDTO getSessionById(UUID id);

    CashierSession getSession(UUID id) ;

    List<CashierSessionResponseDTO> getAllSessions();

    List<CashierSessionResponseDTO> getSessionsByCashier(UUID cashierId);


    List<CashierSessionResponseDTO> getSessionsByStatus(Boolean isClosed);

    void deleteSession(UUID id);
}

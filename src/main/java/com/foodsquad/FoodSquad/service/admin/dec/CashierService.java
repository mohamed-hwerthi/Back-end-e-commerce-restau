package com.foodsquad.FoodSquad.service.admin.dec;

import com.foodsquad.FoodSquad.model.dto.CashierDTO;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.entity.Cashier;

import java.util.List;
import java.util.UUID;

public interface CashierService {

    PaginatedResponseDTO<CashierDTO> findAllCashiers(int page, int limit);

    List<CashierDTO> findAllCashiers();

    CashierDTO findCashierById(UUID id);

    CashierDTO findCashierByCashierId(UUID cashierId);

    CashierDTO createCashier(CashierDTO cashierDTO);

    CashierDTO updateCashier(UUID id, CashierDTO cashierDTO);

    void deleteCashier(UUID id);

    Cashier findCashier(UUID id);

    Cashier saveCashier(Cashier cashier);

}

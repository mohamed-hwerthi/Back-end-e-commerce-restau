package com.foodsquad.FoodSquad.service.declaration;

import com.foodsquad.FoodSquad.model.dto.CashierDTO;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.entity.Cashier;

import java.util.List;

public interface CashierService {

    PaginatedResponseDTO<CashierDTO> findAllCashiers(int page, int limit);

    List<CashierDTO> findAllCashiers();

    CashierDTO findCashierById(String id);

    CashierDTO findCashierByCashierId(String cashierId);

    CashierDTO createCashier(CashierDTO cashierDTO);

    CashierDTO updateCashier(String id, CashierDTO cashierDTO);

    void deleteCashier(String id);

    Cashier findCashier(String id);

    Cashier saveCashier(Cashier cashier);

}

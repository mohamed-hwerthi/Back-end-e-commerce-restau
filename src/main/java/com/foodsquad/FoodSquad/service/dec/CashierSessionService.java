package com.foodsquad.FoodSquad.service.dec;

import com.foodsquad.FoodSquad.model.entity.CashierSession;

import java.util.UUID;

public interface CashierSessionService {
    CashierSession getSessionById(UUID sessionId);
}

package com.foodsquad.FoodSquad.service.admin.dec;

import com.foodsquad.FoodSquad.model.dto.OrderDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {

    List<OrderDTO> searchOrders(String statusCode, LocalDateTime startDate, LocalDateTime endDate);

}

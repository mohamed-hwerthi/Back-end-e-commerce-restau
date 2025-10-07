package com.foodsquad.FoodSquad.controller.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/orders")
@Tag(name = "4. Order Management", description = "Order Management API")
public class OrderController {


}

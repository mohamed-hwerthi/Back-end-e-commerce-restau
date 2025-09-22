package com.foodsquad.FoodSquad.controller;

import com.foodsquad.FoodSquad.service.declaration.ProductAttributeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product-attributes")
public class ProductAttributeController {

    private final ProductAttributeService productAttributeService;


}

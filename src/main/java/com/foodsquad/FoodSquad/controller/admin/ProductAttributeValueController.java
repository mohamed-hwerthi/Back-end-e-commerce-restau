package com.foodsquad.FoodSquad.controller.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/attribute-values")
@RequiredArgsConstructor
@Tag(name = "Attribute Values", description = "APIs for managing attribute values")
public class ProductAttributeValueController {

}

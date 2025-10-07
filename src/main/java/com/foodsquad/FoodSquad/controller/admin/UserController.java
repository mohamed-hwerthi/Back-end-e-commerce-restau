package com.foodsquad.FoodSquad.controller.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/users")
@Tag(name = "3. User Management", description = "User Management API")
public class UserController {

}

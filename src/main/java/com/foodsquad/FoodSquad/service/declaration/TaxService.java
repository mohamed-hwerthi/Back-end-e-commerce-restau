package com.foodsquad.FoodSquad.service.declaration;

import com.foodsquad.FoodSquad.model.dto.MenuItemDTO;
import com.foodsquad.FoodSquad.model.entity.Tax;

public interface TaxService {

    Tax createTax(MenuItemDTO menuItemDTO);

}

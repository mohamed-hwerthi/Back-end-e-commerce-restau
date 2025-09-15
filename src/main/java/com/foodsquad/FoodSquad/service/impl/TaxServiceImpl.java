package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.model.dto.MenuItemDTO;
import com.foodsquad.FoodSquad.model.entity.Tax;
import com.foodsquad.FoodSquad.service.declaration.TaxService;
import org.springframework.stereotype.Service;

@Service
public class TaxServiceImpl implements TaxService {
    @Override
    public Tax createTax(MenuItemDTO menuItemDTO) {
        Tax tax = new Tax();
        tax.setRate(menuItemDTO.getTax().getRate());
        tax.setName(menuItemDTO.getTax().getName() != null ?
                menuItemDTO.getTax().getName() : "TVA");
        return tax;

    }
}

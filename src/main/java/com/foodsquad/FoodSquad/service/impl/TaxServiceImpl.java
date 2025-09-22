package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.model.dto.ProductDTO;
import com.foodsquad.FoodSquad.model.entity.Tax;
import com.foodsquad.FoodSquad.service.declaration.TaxService;
import org.springframework.stereotype.Service;

@Service
public class TaxServiceImpl implements TaxService {
    @Override
    public Tax createTax(ProductDTO productDTO) {
        Tax tax = new Tax();
        tax.setRate(productDTO.getTax().getRate());
        tax.setName(productDTO.getTax().getName() != null ?
                productDTO.getTax().getName() : "TVA");
        return tax;

    }
}

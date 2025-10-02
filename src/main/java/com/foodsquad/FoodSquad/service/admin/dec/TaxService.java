package com.foodsquad.FoodSquad.service.admin.dec;

import com.foodsquad.FoodSquad.model.dto.ProductDTO;
import com.foodsquad.FoodSquad.model.entity.Tax;

public interface TaxService {

    Tax createTax(ProductDTO productDTO);

}

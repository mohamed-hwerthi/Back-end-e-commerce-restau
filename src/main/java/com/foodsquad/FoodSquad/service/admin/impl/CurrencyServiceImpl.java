package com.foodsquad.FoodSquad.service.admin.impl;

import com.foodsquad.FoodSquad.mapper.CurrencyMapper;
import com.foodsquad.FoodSquad.model.dto.CurrencyDTO;
import com.foodsquad.FoodSquad.repository.CurrencyRepository;
import com.foodsquad.FoodSquad.service.admin.dec.CurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final CurrencyMapper currencyMapper;


    @Override
    public List<CurrencyDTO> findAllCurrency() {
        return currencyRepository.findAll().stream().map(currencyMapper::toDto).toList();

    }
}

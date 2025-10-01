package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.dto.ProductOptionDTO;
import com.foodsquad.FoodSquad.mapper.ProductOptionMapper;
import com.foodsquad.FoodSquad.model.entity.ProductOption;
import com.foodsquad.FoodSquad.repository.ProductOptionRepository;
import com.foodsquad.FoodSquad.service.ProductOptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductOptionServiceImpl implements ProductOptionService {


    private final ProductOptionRepository productOptionRepository;

    private final ProductOptionMapper productOptionMapper;

    @Override
    public List<ProductOptionDTO> getAllProductOptions() {
        log.info("Fetching all supplement options from the database");
        return productOptionMapper.toDtoList(productOptionRepository.findAll());
    }

    @Override
    public ProductOptionDTO createProductOption(ProductOptionDTO supplementOptionDTO) {
        log.info("Creating a new supplement option: {}", supplementOptionDTO);
        ProductOption supplementOption = productOptionMapper.toEntity(supplementOptionDTO);
        ProductOption savedOption = productOptionRepository.save(supplementOption);
        return productOptionMapper.toDto(savedOption);
    }

    @Override
    public void deleteProductOption(UUID id) {
        log.info("Deleting supplement option with ID: {}", id);
        productOptionRepository.deleteById(id);
    }
}

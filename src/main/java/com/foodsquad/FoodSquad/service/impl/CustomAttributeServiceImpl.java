package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.mapper.CustomAttributeMapper;
import com.foodsquad.FoodSquad.model.dto.CustomAttributeDTO;
import com.foodsquad.FoodSquad.model.entity.CustomAttribute;
import com.foodsquad.FoodSquad.model.entity.Product;
import com.foodsquad.FoodSquad.repository.CustomAttributeRepository;
import com.foodsquad.FoodSquad.repository.ProductRepository;
import com.foodsquad.FoodSquad.service.declaration.CustomAttributeService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomAttributeServiceImpl implements CustomAttributeService {

    private static final Logger logger = LoggerFactory.getLogger(CustomAttributeServiceImpl.class);

    private final CustomAttributeRepository repository;
    private final ProductRepository productRepository;
    private final CustomAttributeMapper mapper;



    @Override
    public List<CustomAttributeDTO> findAll() {
        logger. info("Fetching all CustomAttributes");
        List<CustomAttributeDTO> result = repository.findAll().stream()
                .map(mapper::toDto)
                .toList();
        logger.info("Found {} CustomAttributes", result.size());
        return result;
    }

    @Override
    public CustomAttributeDTO findById(UUID id) {
        logger.info("Finding CustomAttribute by id: {}", id);
        CustomAttributeDTO result = repository.findById(id)
                .map(mapper::toDto)
                .orElse(null);
        if (result != null) {
            logger.info("Found CustomAttribute: {}", result);
        } else {
            logger.warn("No CustomAttribute found with id: {}", id);
        }
        return result;
    }

    @Override
    public void delete(UUID id) {
        logger.info("Deleting CustomAttribute with id: {}", id);
        repository.deleteById(id);
        logger.info("Deleted CustomAttribute with id: {}", id);
    }


}

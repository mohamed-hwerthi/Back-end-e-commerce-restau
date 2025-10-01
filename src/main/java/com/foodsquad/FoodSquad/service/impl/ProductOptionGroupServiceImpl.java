package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.dto.ProductOptionGroupDTO;
import com.foodsquad.FoodSquad.mapper.ProductOptionGroupMapper;
import com.foodsquad.FoodSquad.model.entity.ProductOptionGroup;
import com.foodsquad.FoodSquad.repository.ProductOptionGroupRepository;
import com.foodsquad.FoodSquad.service.declaration.ProductOptionGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductOptionGroupServiceImpl implements ProductOptionGroupService {

    private static final Logger logger = LoggerFactory.getLogger(ProductOptionGroupServiceImpl.class);

    @Autowired
    private ProductOptionGroupRepository productOptionGroupRepository;

    @Autowired
    private ProductOptionGroupMapper productOptionGroupMapper;

    @Override
    public List<ProductOptionGroupDTO> getAllProductOptionGroups() {
        logger.info("Fetching all supplement groups from the database");
        return productOptionGroupRepository.findAll()
                .stream()
                .map(productOptionGroupMapper::toDto)
                .toList();
    }

    @Override
    public ProductOptionGroupDTO createProductOptionGroup(ProductOptionGroupDTO supplementGroupDTO) {
        logger.info("Creating a new supplement group: {}", supplementGroupDTO);
        ProductOptionGroup supplementGroupEntity = productOptionGroupMapper.toEntity(supplementGroupDTO);
        ProductOptionGroup savedEntity = productOptionGroupRepository.save(supplementGroupEntity);
        return productOptionGroupMapper.toDto(savedEntity);
    }

    @Override
    public void deleteProductOptionGroup(UUID id) {
        logger.info("Deleting supplement group with ID: {}", id);
        if (productOptionGroupRepository.existsById(id)) {
            productOptionGroupRepository.deleteById(id);
        } else {
            logger.warn("Supplement group with ID {} not found", id);
            throw new IllegalArgumentException("Supplement group not found");
        }
    }

    @Override
    public ProductOptionGroupDTO getProductOptionGroupById(UUID id) {
        logger.info("Fetching supplement group with ID: {}", id);
        return productOptionGroupRepository.findById(id)
                .map(productOptionGroupMapper::toDto)
                .orElseThrow(() -> {
                    logger.warn("Supplement group with ID {} not found", id);
                    return new IllegalArgumentException("Supplement group not found");
                });
    }

    @Override
    public ProductOptionGroupDTO updateProductOptionGroup(UUID id, ProductOptionGroupDTO supplementGroupDTO) {
        logger.info("Updating supplement group with ID: {}", id);
        if (!productOptionGroupRepository.existsById(id)) {
            logger.warn("Supplement group with ID {} not found", id);
            throw new IllegalArgumentException("Supplement group not found");
        }
        ProductOptionGroup supplementGroupEntity = productOptionGroupMapper.toEntity(supplementGroupDTO);
        supplementGroupEntity.setId(id);
        ProductOptionGroup updatedEntity = productOptionGroupRepository.save(supplementGroupEntity);
        return productOptionGroupMapper.toDto(updatedEntity);
    }


}

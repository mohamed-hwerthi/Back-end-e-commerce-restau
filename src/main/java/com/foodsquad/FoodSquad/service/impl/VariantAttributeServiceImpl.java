package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.mapper.VariantAttributeMapper;
import com.foodsquad.FoodSquad.model.dto.VariantAttributeDTO;
import com.foodsquad.FoodSquad.model.entity.VariantAttribute;
import com.foodsquad.FoodSquad.repository.AttributeValueRepository;
import com.foodsquad.FoodSquad.repository.ProductVariantRepository;
import com.foodsquad.FoodSquad.repository.VariantAttributeRepository;
import com.foodsquad.FoodSquad.service.declaration.VariantAttributeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class VariantAttributeServiceImpl implements VariantAttributeService {

    private final VariantAttributeRepository variantAttributeRepository;
    private final ProductVariantRepository productVariantRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final VariantAttributeMapper variantAttributeMapper;


    @Override
    public VariantAttributeDTO getVariantAttributeById(UUID id) {
        log.info("Fetching variant attribute with ID: {}", id);
        VariantAttribute variantAttribute = variantAttributeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Variant attribute not found with ID: " + id));
        return variantAttributeMapper.toDto(variantAttribute);
    }

    @Override
    public List<VariantAttributeDTO> getVariantAttributesByVariantId(UUID variantId) {
        log.info("Fetching all attributes for variant ID: {}", variantId);
        return variantAttributeRepository.findByVariantId(variantId).stream()
                .map(variantAttributeMapper::toDto)
                .toList();
    }


    @Override
    @Transactional
    public void deleteVariantAttribute(UUID id) {
        log.info("Deleting variant attribute with ID: {}", id);
        if (!variantAttributeRepository.existsById(id)) {
            log.warn("Variant attribute with ID {} not found for deletion", id);
            throw new IllegalArgumentException("Variant attribute not found with ID: " + id);
        }
        variantAttributeRepository.deleteById(id);
        log.info("Deleted variant attribute with ID: {}", id);
    }

    @Override
    @Transactional
    public void deleteAllByVariantId(UUID variantId) {
        log.info("Deleting all attributes for variant ID: {}", variantId);
        variantAttributeRepository.deleteByVariantId(variantId);
        log.info("Deleted all attributes for variant ID: {}", variantId);
    }
}

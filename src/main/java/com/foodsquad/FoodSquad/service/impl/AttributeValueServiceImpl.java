package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.mapper.AttributeValueMapper;
import com.foodsquad.FoodSquad.model.dto.AttributeValueDTO;
import com.foodsquad.FoodSquad.model.entity.ProductAttributeValue;
import com.foodsquad.FoodSquad.repository.AttributeValueRepository;
import com.foodsquad.FoodSquad.service.declaration.ProductAttributeValueService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttributeValueServiceImpl implements ProductAttributeValueService {

    private final AttributeValueRepository attributeValueRepository;
    private final AttributeValueMapper attributeValueMapper;

    @Override
    @Transactional
    public AttributeValueDTO createAttributeValue(AttributeValueDTO attributeValueDTO) {
        log.info("Creating new attribute value for type ID: {}", attributeValueDTO.getAttributeTypeId());


        if (attributeValueRepository.existsByValueAndAttributeTypeId(
                attributeValueDTO.getValue(),
                attributeValueDTO.getAttributeTypeId())) {
            throw new IllegalArgumentException("Attribute value already exists for this type");
        }

        ProductAttributeValue attributeValue = attributeValueMapper.toEntity(attributeValueDTO);

        ProductAttributeValue savedValue = attributeValueRepository.save(attributeValue);
        log.info("Created attribute value with ID: {}", savedValue.getId());
        return attributeValueMapper.toDto(savedValue);
    }

    @Override
    public AttributeValueDTO getAttributeValueById(UUID id) {
        log.info("Fetching attribute value with ID: {}", id);
        ProductAttributeValue attributeValue = attributeValueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Attribute value not found with ID: " + id));
        return attributeValueMapper.toDto(attributeValue);
    }

    @Override
    public List<AttributeValueDTO> getAttributeValuesByType(UUID attributeTypeId) {
        log.info("Fetching all attribute values for type ID: {}", attributeTypeId);
        return attributeValueRepository.findByAttributeTypeId(attributeTypeId).stream()
                .map(attributeValueMapper::toDto)
                .toList();
    }

    @Override
    public List<AttributeValueDTO> getAllAttributeValues() {
        log.info("Fetching all attribute values");
        return attributeValueRepository.findAll().stream()
                .map(attributeValueMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public AttributeValueDTO updateAttributeValue(UUID id, AttributeValueDTO attributeValueDTO) {
        log.info("Updating attribute value with ID: {}", id);

        ProductAttributeValue existingValue = attributeValueRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Attribute value not found with ID: " + id));
        existingValue.setValue(attributeValueDTO.getValue());
        existingValue.setDisplayName(attributeValueDTO.getDisplayName());
        existingValue.setAdditionalData(attributeValueDTO.getAdditionalData());

        ProductAttributeValue updatedValue = attributeValueRepository.save(existingValue);
        log.info("Updated attribute value with ID: {}", id);

        return attributeValueMapper.toDto(updatedValue);
    }

    @Override
    @Transactional
    public void deleteAttributeValue(UUID id) {
        log.info("Deleting attribute value with ID: {}", id);
        if (!attributeValueRepository.existsById(id)) {
            log.warn("Attribute value with ID {} not found for deletion", id);
            throw new IllegalArgumentException("Attribute value not found with ID: " + id);
        }
        attributeValueRepository.deleteById(id);
        log.info("Deleted attribute value with ID: {}", id);
    }

    @Override
    public List<AttributeValueDTO> getAttributeValuesByTypeIn(List<UUID> attributeTypeIds) {
        log.info("Fetching attribute values for types: {}", attributeTypeIds);
        return attributeValueRepository.findByAttributeTypeIdIn(attributeTypeIds).stream()
                .map(attributeValueMapper::toDto)
                .toList();
    }
}

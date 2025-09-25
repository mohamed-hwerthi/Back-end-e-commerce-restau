package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.dto.SupplementGroupDTO;
import com.foodsquad.FoodSquad.mapper.SupplementGroupMapper;
import com.foodsquad.FoodSquad.repository.SupplementGroupRepository;
import com.foodsquad.FoodSquad.service.SupplementGroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SupplementGroupServiceImpl implements SupplementGroupService {

    private static final Logger logger = LoggerFactory.getLogger(SupplementGroupServiceImpl.class);

    @Autowired
    private SupplementGroupRepository supplementGroupRepository;

    @Autowired
    private SupplementGroupMapper supplementGroupMapper;

    @Override
    public List<SupplementGroupDTO> getAllSupplementGroups() {
        logger.info("Fetching all supplement groups from the database");
        return supplementGroupRepository.findAll()
                .stream()
                .map(supplementGroupMapper::toDTO)
                .toList();
    }

    @Override
    public SupplementGroupDTO createSupplementGroup(SupplementGroupDTO supplementGroupDTO) {
        logger.info("Creating a new supplement group: {}", supplementGroupDTO);
        SupplementGroup supplementGroupEntity = supplementGroupMapper.toEntity(supplementGroupDTO);
        SupplementGroup savedEntity = supplementGroupRepository.save(supplementGroupEntity);
        return supplementGroupMapper.toDTO(savedEntity);
    }

    @Override
    public void deleteSupplementGroup(UUID id) {
        logger.info("Deleting supplement group with ID: {}", id);
        if (supplementGroupRepository.existsById(id)) {
            supplementGroupRepository.deleteById(id);
        } else {
            logger.warn("Supplement group with ID {} not found", id);
            throw new IllegalArgumentException("Supplement group not found");
        }
    }

    @Override
    public SupplementGroupDTO getSupplementGroupById(UUID id) {
        logger.info("Fetching supplement group with ID: {}", id);
        return supplementGroupRepository.findById(id)
                .map(supplementGroupMapper::toDTO)
                .orElseThrow(() -> {
                    logger.warn("Supplement group with ID {} not found", id);
                    return new IllegalArgumentException("Supplement group not found");
                });
    }

    @Override
    public SupplementGroupDTO updateSupplementGroup(UUID id, SupplementGroupDTO supplementGroupDTO) {
        logger.info("Updating supplement group with ID: {}", id);
        if (!supplementGroupRepository.existsById(id)) {
            logger.warn("Supplement group with ID {} not found", id);
            throw new IllegalArgumentException("Supplement group not found");
        }
        SupplementGroup supplementGroupEntity = supplementGroupMapper.toEntity(supplementGroupDTO);
        supplementGroupEntity.setId(id);
        SupplementGroup updatedEntity = supplementGroupRepository.save(supplementGroupEntity);
        return supplementGroupMapper.toDTO(updatedEntity);
    }

    @Override
    public List<SupplementGroupDTO> getSupplementGroupsByOption(String optionName) {
        logger.info("Fetching supplement groups with option: {}", optionName);
        return supplementGroupRepository.findByOptionName(optionName)
                .stream()
                .map(supplementGroupMapper::toDTO)
                .toList();
    }
}

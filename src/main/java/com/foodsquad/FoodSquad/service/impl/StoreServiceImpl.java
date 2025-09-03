package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.mapper.StoreMapper;
import com.foodsquad.FoodSquad.model.dto.StoreDTO;
import com.foodsquad.FoodSquad.model.entity.Store;
import com.foodsquad.FoodSquad.model.entity.User;
import com.foodsquad.FoodSquad.repository.StoreRepository;
import com.foodsquad.FoodSquad.service.declaration.AdminService;
import com.foodsquad.FoodSquad.service.declaration.StoreService;
import com.foodsquad.FoodSquad.service.declaration.TenantService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    private static final Logger logger = LoggerFactory.getLogger(StoreServiceImpl.class);

    private final StoreRepository storeRepository;

    private final StoreMapper storeMapper;

    private final TenantService tenantService;

    private final AdminService adminService;

    @Override
    @Transactional
    public StoreDTO save(  @Valid StoreDTO storeDTO) {
        logger.info("Saving new Store: {}", storeDTO);
        Store store = storeMapper.toEntity(storeDTO);
        User savedOwner =  adminService.createStoreOwner(storeDTO.getEmail(), storeDTO.getPhoneNumber(), storeDTO.getPassword()) ;
        store.setOwner(savedOwner);
        Store saved = storeRepository.save(store);
            tenantService.createTenant(saved.getId().toString());
        StoreDTO result = storeMapper.toDto(saved);
        logger.info("Saved Store with id: {}", result.getId());
        return result;
    }

    @Override
    public StoreDTO update(UUID storeId, StoreDTO storeDTO) {
        logger.info("Updating Store with id: {}", storeId);
        return storeRepository.findById(storeId)
                .map(existing -> {
                    Store updated = storeMapper.toEntity(storeDTO);
                    updated.setId(existing.getId());
                    Store saved = storeRepository.save(updated);
                    logger.info("Updated Store with id: {}", storeId);
                    return storeMapper.toDto(saved);
                })
                .orElseGet(() -> {
                    logger.warn("Cannot update, Store not found with id: {}", storeId);
                    return null;
                });
    }

    @Override
    public List<StoreDTO> findAll() {
        logger.info("Fetching all Stores");
        return storeRepository.findAll().stream()
                .map(storeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public StoreDTO findById(UUID id) {
        logger.info("Fetching Store with id: {}", id);
        return storeRepository.findById(id)
                .map(storeMapper::toDto)
                .orElse(null);
    }

    @Override
    public void delete(UUID id) {
        logger.info("Deleting Store with id: {}", id);
        storeRepository.deleteById(id);
        logger.info("Deleted Store with id: {}", id);
    }


    @Override
    public StoreDTO findByOwner(User owner) {
        logger.info("   Request to Fetch  Store by owner: {}", owner);
        return storeRepository.findByOwner(owner).map(storeMapper::toDto).orElseThrow(() -> new EntityNotFoundException("Store not found for owner with email " +  owner.getEmail()));

    }
}

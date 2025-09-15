package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.config.security.EncryptionUtil;
import com.foodsquad.FoodSquad.mapper.CurrencyMapper;
import com.foodsquad.FoodSquad.mapper.StoreMapper;
import com.foodsquad.FoodSquad.model.dto.CurrencyDTO;
import com.foodsquad.FoodSquad.model.dto.StoreBasicDataDTO;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreServiceImpl implements StoreService {


    private final StoreRepository storeRepository;

    private final StoreMapper storeMapper;

    private final TenantService tenantService;

    private final AdminService adminService;

    private final DataSource dataSource;

    private final CurrencyMapper currencyMapper;


    @Override
    @Transactional
    public StoreDTO save(@Valid StoreDTO storeDTO) {
        log.info("Saving new Store: {}", storeDTO);
        Store store = storeMapper.toEntity(storeDTO);
        User savedOwner = adminService.createStoreOwner(storeDTO.getEmail(), storeDTO.getPhoneNumber(), storeDTO.getPassword());
        store.setOwner(savedOwner);
        String slug = generateUniqueSlug(storeDTO.getName());
        store.setSlug(slug);
        Store saved = storeRepository.save(store);
        tenantService.createTenant(saved.getId().toString(), savedOwner);
        String encryptedStoreId = encryptStoreId(saved.getId().toString());
        StoreDTO result = storeMapper.toDto(saved);
        result.setEncryptedStoreId(encryptedStoreId);
        log.info("Saved Store with id: {}", result.getId());
        return result;
    }

    @Override
    public StoreDTO update(UUID storeId, StoreDTO storeDTO) {
        log.info("Updating Store with id: {}", storeId);
        return storeRepository.findById(storeId)
                .map(existing -> {
                    Store updated = storeMapper.toEntity(storeDTO);
                    updated.setId(existing.getId());
                    Store saved = storeRepository.save(updated);
                    log.info("Updated Store with id: {}", storeId);
                    return storeMapper.toDto(saved);
                })
                .orElseGet(() -> {
                    log.warn("Cannot update, Store not found with id: {}", storeId);
                    return null;
                });
    }

    @Override
    public List<StoreDTO> findAll() {
        log.info("Fetching all Stores");
        return storeRepository.findAll().stream()
                .map(storeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public StoreDTO findById(UUID id) {
        log.info("Fetching Store with id: {}", id);
        return storeRepository.findById(id)
                .map(storeMapper::toDto)
                .orElse(null);
    }

    @Override
    public void delete(UUID id) {
        log.info("Deleting Store with id: {}", id);
        storeRepository.deleteById(id);
        log.info("Deleted Store with id: {}", id);
    }


    @Override
    public StoreDTO findByOwner(User owner) {
        log.info("   Request to Fetch  Store by owner: {}", owner);
        return storeRepository.findByOwner(owner).map(storeMapper::toDto).orElseThrow(() -> new EntityNotFoundException("Store not found for owner with email " + owner.getEmail()));

    }


    @Override
    public StoreBasicDataDTO findByEmail() {
        log.debug("Fetching store for owner with email");
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        User user = adminService.findByEmail(email);
        Store store = storeRepository.findByOwner(user)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Store not found for user with email %s", email))
                );
        return StoreBasicDataDTO.builder()
                .storeId(store.getId())
                .storeName(store.getName())
                .storeSlug(store.getSlug())
                .build();
    }

    @Override
    public CurrencyDTO findCurrencyOfStore(UUID storeId) {
        log.debug("Request to find currency of the store   :{}", storeId);
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new EntityNotFoundException("Store not found with id " + storeId));
        return currencyMapper.toDto(store.getCurrency());

    }

    @Override
    public StoreBasicDataDTO findByStoreSlug(String slug) {
        log.debug("Fetching store with slug: {}", slug);
        Store store = storeRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Store not found for slug %s", slug))
                );
        return StoreBasicDataDTO.builder()
                .storeId(store.getId())
                .storeName(store.getName())
                .storeSlug(store.getSlug())
                .build();
    }


    /**
     * Encrypt store ID
     */
    private String encryptStoreId(String storeId) {
        try {
            return EncryptionUtil.encrypt(storeId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt store ID", e);
        }
    }


    private String generateUniqueSlug(String name) {
        String baseSlug = generateSlug(name);
        String slug = baseSlug;
        int counter = 1;

        while (storeRepository.existsBySlug(slug)) {
            slug = baseSlug + "-" + counter;
            counter++;
        }

        return slug;
    }


    private String generateSlug(String name) {
        return name.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", "-");
    }

    private void logCurrentSchema() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SHOW search_path")) {

            if (rs.next()) {
                String schema = rs.getString(1);
                log.info("Current database schema (search_path): {}", schema);
            }
        } catch (Exception e) {
            log.error("Failed to get current schema", e);
        }
    }


}



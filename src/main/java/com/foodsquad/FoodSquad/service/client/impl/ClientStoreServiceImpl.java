package com.foodsquad.FoodSquad.service.client.impl;


import com.foodsquad.FoodSquad.mapper.StoreMapper;
import com.foodsquad.FoodSquad.model.dto.client.ClientStoreDTO;
import com.foodsquad.FoodSquad.model.entity.Store;
import com.foodsquad.FoodSquad.repository.StoreRepository;
import com.foodsquad.FoodSquad.service.client.dec.ClientStoreService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementation of ClientStoreService.
 * Handles retrieval of client-facing store data.
 *
 * <p>Éditeur de code: Mohamed Hwerthi</p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ClientStoreServiceImpl implements ClientStoreService {

    private final StoreRepository storeRepository;

    private final StoreMapper storeMapper;


    @Override
    public ClientStoreDTO getStoreBySlug(String slug) {
        log.info("Fetching client store data ");
        Store store = storeRepository.findBySlug(slug)
                .orElseThrow(() -> {
                    log.error("Store not found");
                    return new EntityNotFoundException("Store not found");
                });

        ClientStoreDTO dto = storeMapper.toClientStoreDTO(store);
        log.info("Returning client store DTO :{}", dto);
        return dto;
    }
}

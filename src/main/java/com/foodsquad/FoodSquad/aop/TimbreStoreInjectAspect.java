package com.foodsquad.FoodSquad.aop;

import com.foodsquad.FoodSquad.config.StoreContextHolder;
import com.foodsquad.FoodSquad.mapper.StoreMapper;
import com.foodsquad.FoodSquad.model.dto.TimbreDTO;
import com.foodsquad.FoodSquad.model.entity.Store;
import com.foodsquad.FoodSquad.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class TimbreStoreInjectAspect {

    private final StoreRepository storeRepository;

    private final StoreMapper storeMapper ;
    @Before("execution(* com.foodsquad.FoodSquad.service.declaration.TimbreService.save(..)) && args(timbreDTO)")
    public void injectStoreBeforeSave(TimbreDTO timbreDTO) {
        String storeId = StoreContextHolder.getStoreId();
        if(storeId == null) throw new IllegalStateException("storeId not found in context");

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalStateException("Store not found for id: " + storeId));

         timbreDTO.setStore(storeMapper.toDto(store));

    }

    @Before("execution(* com.foodsquad.FoodSquad.service.declaration.TimbreService.update(..)) && args(id, timbreDTO)")
    public void injectStoreBeforeUpdate(String id, TimbreDTO timbreDTO) {
        String storeId = StoreContextHolder.getStoreId();
        if(storeId == null) throw new IllegalStateException("storeId not found in context");

        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new IllegalStateException("Store not found for id: " + storeId));

        timbreDTO.setStore(storeMapper.toDto(store));
    }
}

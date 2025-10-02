package com.foodsquad.FoodSquad.service;

import com.foodsquad.FoodSquad.model.dto.TimbreDTO;
import com.foodsquad.FoodSquad.service.admin.dec.TimbreService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class TimbreMultiTenantIntegrationTest {

    @Autowired
    private TimbreService timbreService;

    @Autowired
    private StoreContextHolder storeContextHolder;

    @BeforeEach
    void setupStores() {
        storeContextHolder.clear();

        storeContextHolder.setStoreId("store1");
        timbreService.save(new TimbreDTO(null, 100.00));
        timbreService.save(new TimbreDTO(null, 200.0, null));

        storeContextHolder.setStoreId("store2");
        timbreService.save(new TimbreDTO(null, 300.0, null));
    }

    @Test
    void whenQueryingByStore1_onlyStore1TimbresReturned() {
        storeContextHolder.setStoreId("store1");
        List<TimbreDTO> timbres = timbreService.findAll();
        assertEquals(2, timbres.size());
    }

    @Test
    void whenQueryingByStore2_onlyStore2TimbresReturned() {
        storeContextHolder.setStoreId("store2");
        List<TimbreDTO> timbres = timbreService.findAll();
        assertEquals(1, timbres.size());
    }
}

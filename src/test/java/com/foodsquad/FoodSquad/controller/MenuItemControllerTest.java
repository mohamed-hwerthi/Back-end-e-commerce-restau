package com.foodsquad.FoodSquad.controller;

import com.foodsquad.FoodSquad.model.dto.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class MenuItemControllerTest {

    @Mock
    private com.foodsquad.FoodSquad.service.declaration.MenuItemService menuItemService;

    @InjectMocks
    private MenuItemController menuItemController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testCreateMenuItem() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setTitle("Burger");
        when(menuItemService.createMenuItem(any(ProductDTO.class))).thenReturn(ResponseEntity.ok(productDTO));

        ResponseEntity<ProductDTO> response = menuItemController.createMenuItem(productDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDTO, response.getBody());
        verify(menuItemService, times(1)).createMenuItem(any(ProductDTO.class));
    }

    @Test
    void testGetMenuItemById() {
        ProductDTO productDTO = new ProductDTO();
        when(menuItemService.getMenuItemById(anyLong())).thenReturn(productDTO);

        ResponseEntity<ProductDTO> response = menuItemController.getMenuItemById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDTO, response.getBody());
        verify(menuItemService, times(1)).getMenuItemById(1L);
    }

    @Test
    void testUpdateMenuItem() {
        ProductDTO productDTO = new ProductDTO();
        when(menuItemService.updateMenuItem(anyLong(), any(ProductDTO.class))).thenReturn(ResponseEntity.ok(productDTO));

        ResponseEntity<ProductDTO> response = menuItemController.updateMenuItem(1L, productDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDTO, response.getBody());
        verify(menuItemService, times(1)).updateMenuItem(anyLong(), any(ProductDTO.class));
    }

    @Test
    void testDeleteMenuItem() {
        when(menuItemService.deleteMenuItem(anyLong())).thenReturn(ResponseEntity.ok(Collections.singletonMap("message", "Menu Item successfully deleted")));

        ResponseEntity<Map<String, String>> response = menuItemController.deleteMenuItem(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Menu Item successfully deleted", response.getBody().get("message"));
        verify(menuItemService, times(1)).deleteMenuItem(1L);
    }

    @Test
    void testGetMenuItemsByIds() {
        List<ProductDTO> products = Collections.singletonList(new ProductDTO());
        when(menuItemService.getMenuItemsByIds(anyList())).thenReturn(ResponseEntity.ok(products));

        List<Long> menuItemIds = List.of(1L, 2L, 3L);
        ResponseEntity<List<ProductDTO>> response = menuItemController.getMenuItemsByIds(menuItemIds);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(products, response.getBody());
        verify(menuItemService, times(1)).getMenuItemsByIds(anyList());
    }


    @Test
    void testDeleteMenuItemsByIds() {
        when(menuItemService.deleteMenuItemsByIds(anyList())).thenReturn(ResponseEntity.ok(Collections.singletonMap("message", "Menu Items successfully deleted")));

        List<Long> menuItemIds = List.of(1L, 2L, 3L);
        ResponseEntity<Map<String, String>> response = menuItemController.deleteMenuItemsByIds(menuItemIds);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Menu Items successfully deleted", response.getBody().get("message"));
        verify(menuItemService, times(1)).deleteMenuItemsByIds(anyList());
    }

}

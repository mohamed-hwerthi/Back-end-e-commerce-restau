package com.foodsquad.FoodSquad.controller;

import com.foodsquad.FoodSquad.controller.admin.ProductController;
import com.foodsquad.FoodSquad.model.dto.ProductDTO;
import com.foodsquad.FoodSquad.service.admin.dec.ProductService;
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

class ProductControllerTest {

    @Mock
    private ProductService ProductService;

    @InjectMocks
    private ProductController ProductController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testCreateProduct() {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setTitle("Burger");
        when(ProductService.createProduct(any(ProductDTO.class))).thenReturn(ResponseEntity.ok(productDTO));

        ResponseEntity<ProductDTO> response = ProductController.createProduct(productDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDTO, response.getBody());
        verify(ProductService, times(1)).createProduct(any(ProductDTO.class));
    }

    @Test
    void testGetProductById() {
        ProductDTO productDTO = new ProductDTO();
        when(ProductService.getProductById(anyLong())).thenReturn(productDTO);

        ResponseEntity<ProductDTO> response = ProductController.getProductById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDTO, response.getBody());
        verify(ProductService, times(1)).getProductById(1L);
    }

    @Test
    void testUpdateProduct() {
        ProductDTO productDTO = new ProductDTO();
        when(ProductService.updateProduct(anyLong(), any(ProductDTO.class))).thenReturn(ResponseEntity.ok(productDTO));

        ResponseEntity<ProductDTO> response = ProductController.updateProduct(1L, productDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(productDTO, response.getBody());
        verify(ProductService, times(1)).updateProduct(anyLong(), any(ProductDTO.class));
    }

    @Test
    void testDeleteProduct() {
        when(ProductService.deleteProduct(anyLong())).thenReturn(ResponseEntity.ok(Collections.singletonMap("message", "Menu Item successfully deleted")));

        ResponseEntity<Map<String, String>> response = ProductController.deleteProduct(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Menu Item successfully deleted", response.getBody().get("message"));
        verify(ProductService, times(1)).deleteProduct(1L);
    }

    @Test
    void testGetProductsByIds() {
        List<ProductDTO> products = Collections.singletonList(new ProductDTO());
        when(ProductService.getProductsByIds(anyList())).thenReturn(ResponseEntity.ok(products));

        List<Long> ProductIds = List.of(1L, 2L, 3L);
        ResponseEntity<List<ProductDTO>> response = ProductController.getProductsByIds(ProductIds);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(products, response.getBody());
        verify(ProductService, times(1)).getProductsByIds(anyList());
    }


    @Test
    void testDeleteProductsByIds() {
        when(ProductService.deleteProductsByIds(anyList())).thenReturn(ResponseEntity.ok(Collections.singletonMap("message", "Menu Items successfully deleted")));

        List<Long> ProductIds = List.of(1L, 2L, 3L);
        ResponseEntity<Map<String, String>> response = ProductController.deleteProductsByIds(ProductIds);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Menu Items successfully deleted", response.getBody().get("message"));
        verify(ProductService, times(1)).deleteProductsByIds(anyList());
    }

}

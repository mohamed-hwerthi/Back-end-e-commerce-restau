package com.foodsquad.FoodSquad.service;

import com.foodsquad.FoodSquad.model.dto.ProductDTO;
import com.foodsquad.FoodSquad.model.entity.Product;
import com.foodsquad.FoodSquad.model.entity.User;
import com.foodsquad.FoodSquad.model.entity.UserRole;
import com.foodsquad.FoodSquad.repository.ProductRepository;
import com.foodsquad.FoodSquad.repository.OrderRepository;
import com.foodsquad.FoodSquad.repository.ReviewRepository;
import com.foodsquad.FoodSquad.repository.UserRepository;
import com.foodsquad.FoodSquad.service.declaration.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository ProductRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ProductService ProductService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockSecurityContext("test@example.com");
    }

    @Test
    void testCreateProduct() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        productDTO.setTitle("Burger");
        productDTO.setDescription("Delicious burger");
        productDTO.setPrice(10.0);

        User user = new User();
        user.setEmail("test@example.com");

        Product product = new Product();
        product.setId(1L);
        product.setUser(user);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(ProductRepository.save(any(Product.class))).thenReturn(product);
        when(modelMapper.map(any(ProductDTO.class), eq(Product.class))).thenReturn(product);
        when(modelMapper.map(any(Product.class), eq(ProductDTO.class))).thenReturn(productDTO);

        // Act
        ResponseEntity<ProductDTO> response = ProductService.createProduct(productDTO);

        // Assert
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Burger", response.getBody().getTitle());
    }


    @Test
    void testUpdateProduct() {
        // Arrange
        Long ProductId = 1L;
        ProductDTO productDTO = new ProductDTO();
        productDTO.setTitle("Updated Burger");
        productDTO.setDescription("Updated description");
        productDTO.setPrice(12.0);

        User user = new User();
        user.setEmail("test@example.com");
        user.setRole(UserRole.ADMIN);

        Product existingProduct = new Product();
        existingProduct.setId(ProductId);
        existingProduct.setUser(user);

        TypeMap<ProductDTO, Product> typeMap = mock(TypeMap.class);

        when(ProductRepository.findById(ProductId)).thenReturn(Optional.of(existingProduct));
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(ProductRepository.save(any(Product.class))).thenReturn(existingProduct);
        when(modelMapper.typeMap(ProductDTO.class, Product.class)).thenReturn(typeMap);
        when(modelMapper.map(any(Product.class), eq(ProductDTO.class))).thenReturn(productDTO);

        // Act
        ResponseEntity<ProductDTO> response = ProductService.updateProduct(ProductId, productDTO);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Updated Burger", response.getBody().getTitle());
        assertEquals("Updated description", response.getBody().getDescription());
        assertEquals(12.0, response.getBody().getPrice());
    }

    @Test
    void testDeleteProduct() {
        // Arrange
        Long ProductId = 1L;
        User user = new User();
        user.setEmail("test@example.com");
        user.setRole(UserRole.EMPLOYEE);

        Product product = new Product();
        product.setId(ProductId);
        product.setUser(user);

        when(ProductRepository.findById(ProductId)).thenReturn(Optional.of(product));
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        // Act
        ResponseEntity<Map<String, String>> response = ProductService.deleteProduct(ProductId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Menu Item successfully deleted", response.getBody().get("message"));
        verify(ProductRepository, times(1)).delete(product);
    }

    private void mockSecurityContext(String email) {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(email);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, "", Collections.emptyList()));
    }
}

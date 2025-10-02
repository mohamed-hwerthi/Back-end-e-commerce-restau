package com.foodsquad.FoodSquad.service;

import com.foodsquad.FoodSquad.model.dto.ReviewDTO;
import com.foodsquad.FoodSquad.model.entity.Product;
import com.foodsquad.FoodSquad.model.entity.Review;
import com.foodsquad.FoodSquad.model.entity.User;
import com.foodsquad.FoodSquad.repository.ProductRepository;
import com.foodsquad.FoodSquad.repository.ReviewRepository;
import com.foodsquad.FoodSquad.repository.UserRepository;
import com.foodsquad.FoodSquad.service.impl.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ProductRepository ProductRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockSecurityContext("test@example.com");
    }

    @Test
    void testCreateReview() {
        // Arrange
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setProductId(1L);
        reviewDTO.setComment("Great food!");
        reviewDTO.setRating(5);

        Product product = new Product();
        product.setId(1L);

        User user = new User();
        user.setEmail("test@example.com");

        Review review = new Review();
        review.setComment("Great food!");
        review.setRating(5);
        review.setProduct(product);
        review.setUser(user);

        when(ProductRepository.findById(1L)).thenReturn(Optional.of(product));
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(modelMapper.map(any(Review.class), eq(ReviewDTO.class))).thenReturn(reviewDTO);

        // Act
        ReviewDTO result = reviewService.createReview(reviewDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Great food!", result.getComment());
        assertEquals(5, result.getRating());
    }

    @Test
    void testGetReviewsByProductId() {
        // Arrange
        Long ProductId = 1L;
        Review review = new Review();
        review.setComment("Great food!");
        review.setRating(5);

        when(reviewRepository.findByProductId(ProductId)).thenReturn(List.of(review));
        when(modelMapper.map(any(Review.class), eq(ReviewDTO.class))).thenReturn(new ReviewDTO());

        // Act
        List<ReviewDTO> result = reviewService.getReviewsByProductId(ProductId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetReviewsByUserId() {
        // Arrange
        String userId = "user123";
        Review review = new Review();
        review.setComment("Great food!");
        review.setRating(5);

        when(reviewRepository.findByUserIdOrderByCreatedAtDesc(eq(userId), any(Pageable.class))).thenReturn(List.of(review));
        when(modelMapper.map(any(Review.class), eq(ReviewDTO.class))).thenReturn(new ReviewDTO());

        // Act
        List<ReviewDTO> result = reviewService.getReviewsByUserId(userId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testUpdateReview() {
        // Arrange
        Long reviewId = 1L;
        ReviewDTO reviewDTO = new ReviewDTO();
        reviewDTO.setComment("Updated comment");
        reviewDTO.setRating(4);

        Review existingReview = new Review();
        existingReview.setComment("Great food!");
        existingReview.setRating(5);

        Review updatedReview = new Review();
        updatedReview.setComment("Updated comment");
        updatedReview.setRating(4);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(existingReview));
        when(reviewRepository.save(existingReview)).thenReturn(updatedReview);
        when(modelMapper.map(any(Review.class), eq(ReviewDTO.class))).thenReturn(reviewDTO);

        // Act
        ReviewDTO result = reviewService.updateReview(reviewId, reviewDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Updated comment", result.getComment());
        assertEquals(4, result.getRating());
    }

    @Test
    void testDeleteReview() {
        // Arrange
        Long reviewId = 1L;
        Review review = new Review();
        review.setId(reviewId);

        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));

        // Act
        reviewService.deleteReview(reviewId);

        // Assert
        verify(reviewRepository, times(1)).delete(review);
    }

    private void mockSecurityContext(String email) {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(email);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, "", Collections.emptyList()));
    }
}

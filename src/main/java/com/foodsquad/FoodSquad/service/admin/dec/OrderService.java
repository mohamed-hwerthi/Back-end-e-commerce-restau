package com.foodsquad.FoodSquad.service.admin.dec;

import com.foodsquad.FoodSquad.model.dto.OrderDTO;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service interface for managing orders in the admin panel.
 * <p>
 * Provides methods to search, filter, and paginate orders.
 * </p>
 */
public interface OrderService {

    /**
     * Searches orders based on optional filters with pagination support.
     *
     * @param statusCode optional status code to filter orders (e.g., PAID, PENDING)
     * @param startDate  optional start date to filter orders created after this date
     * @param endDate    optional end date to filter orders created before this date
     * @param source     optional order source (WEBSITE, POS, ADMIN_PANEL)
     * @param pageable   pagination information (page number, page size, sort)
     * @return a {@link Page} of {@link OrderDTO} matching the filters
     */
    PaginatedResponseDTO<OrderDTO> searchOrders(String statusCode,
                                                LocalDateTime startDate,
                                                LocalDateTime endDate,
                                                String source,
                                                Pageable pageable);





    /**
     * Retrieves a single order by its ID.
     */
    OrderDTO getOrderById(UUID id);
}

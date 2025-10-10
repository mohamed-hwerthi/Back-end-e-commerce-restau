package com.foodsquad.FoodSquad.service.client.dec;

import com.foodsquad.FoodSquad.model.dto.client.ClientOrderDTO;
import com.foodsquad.FoodSquad.model.entity.Customer;


/**
 * Service interface for managing customers.
 * Provides methods for CRUD operations and querying customers.
 */
public interface ClientCustomerService {

    /**
     * Find an existing customer by ID, email, or phone, or create a new one.
     * Used by ClientOrderService when placing an order.
     *
     * @param clientOrderDTO DTO containing customer info.
     * @return Existing or newly created Customer entity.
     */
    Customer findOrCreateCustomerFromOrder(ClientOrderDTO clientOrderDTO);


}

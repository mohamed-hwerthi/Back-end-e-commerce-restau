package com.foodsquad.FoodSquad.service.admin.dec;

import com.foodsquad.FoodSquad.model.dto.CustomerDTO;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CustomerService {
    CustomerDTO createCustomer(CustomerDTO customerDTO);

    CustomerDTO getCustomerById(UUID id);

    PaginatedResponseDTO<CustomerDTO> getAllCustomers(Pageable pageable);

    CustomerDTO updateCustomer(UUID id, CustomerDTO customerDTO);

    void deleteCustomer(UUID id);
}

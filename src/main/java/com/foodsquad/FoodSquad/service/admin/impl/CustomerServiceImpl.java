package com.foodsquad.FoodSquad.service.admin.impl;

import com.foodsquad.FoodSquad.mapper.CustomerMapper;
import com.foodsquad.FoodSquad.model.dto.CustomerDTO;
import com.foodsquad.FoodSquad.model.entity.Customer;
import com.foodsquad.FoodSquad.repository.CustomerRepository;
import com.foodsquad.FoodSquad.service.admin.dec.CustomerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    @Transactional
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        log.info("Creating new customer with email: {}", customerDTO.getEmail());
        Customer customer = customerMapper.toEntity(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        log.info("Successfully created customer with ID: {}", savedCustomer.getId());
        return customerMapper.toDto(savedCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerDTO getCustomerById(UUID id) {
        log.debug("Fetching customer with ID: {}", id);
        return customerRepository.findById(id)
                .map(customerMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerDTO> getAllCustomers(Pageable pageable) {
        log.debug("Fetching all customers with pagination - Page: {}, Size: {}", 
                 pageable.getPageNumber(), pageable.getPageSize());
        return customerRepository.findAll(pageable)
                .map(customerMapper::toDto);
    }

    @Override
    @Transactional
    public CustomerDTO updateCustomer(UUID id, CustomerDTO customerDTO) {
        log.info("Updating customer with ID: {}", id);
        return customerRepository.findById(id)
                .map(existingCustomer -> {
                    customerMapper.updateEntityFromDto(customerDTO, existingCustomer);
                    Customer updatedCustomer = customerRepository.save(existingCustomer);
                    log.info("Successfully updated customer with ID: {}", id);
                    return customerMapper.toDto(updatedCustomer);
                })
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + id));
    }

    @Override
    @Transactional
    public void deleteCustomer(UUID id) {
        log.info("Deleting customer with ID: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with ID: " + id));
        customerRepository.delete(customer);
        log.info("Successfully deleted customer with ID: {}", id);
    }
}

package com.foodsquad.FoodSquad.service.client.impl;

import com.foodsquad.FoodSquad.model.dto.client.ClientCustomerDTO;
import com.foodsquad.FoodSquad.model.dto.client.ClientOrderDTO;
import com.foodsquad.FoodSquad.model.entity.Customer;
import com.foodsquad.FoodSquad.repository.CustomerRepository;
import com.foodsquad.FoodSquad.service.client.dec.ClientCustomerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientCustomerServiceImpl implements ClientCustomerService {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public Customer findOrCreateCustomerFromOrder(ClientOrderDTO clientOrderDTO) {

        return findExistingCustomer(clientOrderDTO.getCustomer())
                .orElseGet(() -> createCustomer(clientOrderDTO.getCustomer()));
    }


    private Optional<Customer> findExistingCustomer(ClientCustomerDTO customerDTO) {
        return findById(customerDTO.getId())
                .or(() -> findByEmail(customerDTO.getEmail()))
                .or(() -> findByPhone(customerDTO.getPhone()));
    }

    @Override
    public boolean existsById(UUID customerId) {
        if (customerId == null) {
            return false;
        }
        return customerRepository.existsById(customerId);
    }

    private Optional<Customer> findById(UUID id) {
        if (id == null) return Optional.empty();

        try {
            return customerRepository.findById(id);
        } catch (EntityNotFoundException ex) {
            log.warn("Customer not found with ID: {}", id);
            return Optional.empty();
        }
    }

    private Optional<Customer> findByEmail(String email) {
        if (!StringUtils.hasText(email)) return Optional.empty();

        return customerRepository.findByEmail(email)
                .map(customer -> {
                    log.info("Found existing customer by email: {}", email);
                    return customer;
                });
    }

    private Optional<Customer> findByPhone(String phone) {
        if (!StringUtils.hasText(phone)) return Optional.empty();

        return customerRepository.findByPhone(phone)
                .map(customer -> {
                    log.info("Found existing customer by phone: {}", phone);
                    return customer;
                });
    }

    private Customer createCustomer(ClientCustomerDTO clientCustomerDTO) {
        Customer newCustomer = Customer.builder()
                .firstName(clientCustomerDTO.getFirstName())
                .lastName(clientCustomerDTO.getLastName())
                .email(clientCustomerDTO.getEmail())
                .phone(clientCustomerDTO.getPhone())
                .build();

        log.info("Creating new customer: {} {}", newCustomer.getFirstName(), newCustomer.getLastName());

        return saveCustomer(newCustomer);
    }

    @Transactional
    private Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
}

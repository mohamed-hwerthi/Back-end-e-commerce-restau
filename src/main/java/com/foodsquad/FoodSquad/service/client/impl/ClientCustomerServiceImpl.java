package com.foodsquad.FoodSquad.service.client.impl;

import com.foodsquad.FoodSquad.model.dto.client.ClientCustomerDTO;
import com.foodsquad.FoodSquad.model.dto.client.ClientOrderDTO;
import com.foodsquad.FoodSquad.model.dto.client.CustomerRegistrationDTO;
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
        ClientCustomerDTO customerDTO = clientOrderDTO.getCustomer();
        Optional<Customer> existingCustomer = findExistingCustomer(customerDTO);
        
        if (existingCustomer.isPresent()) {
            return existingCustomer.get();
        }
        return registerGuestCustomer(
            customerDTO.getEmail(),
            customerDTO.getFirstName(),
            customerDTO.getLastName(),
            customerDTO.getPhone()
        );
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

    @Override
    @Transactional
    public Customer registerCustomer(CustomerRegistrationDTO registrationDTO) {
        // Check if a non-guest user with this email already exists
        customerRepository.findByEmail(registrationDTO.getEmail())
            .ifPresent(customer -> {
                if (!customer.isGuest()) {
                    throw new IllegalStateException("Email already in use");
                }
            });

        // Check if phone is already in use by a non-guest user
        customerRepository.findByPhone(registrationDTO.getPhoneNumber())
            .ifPresent(customer -> {
                if (!customer.isGuest()) {
                    throw new IllegalStateException("Phone number already in use");
                }
            });

        // Check if this is a guest converting to registered user
        return customerRepository.findByEmail(registrationDTO.getEmail())
            .map(guest -> {
                // Update guest to registered user
                guest.convertToRegisteredUser(registrationDTO.getPassword())
                    .setFirstName(registrationDTO.getFirstName())
                    .setLastName(registrationDTO.getLastName())
                    .setPhone(registrationDTO.getPhoneNumber());
                log.info("Converting guest to registered user: {}", registrationDTO.getEmail());
                return saveCustomer(guest);
            })
            .orElseGet(() -> {
                // Create new registered user
                Customer customer = Customer.builder()
                    .firstName(registrationDTO.getFirstName())
                    .lastName(registrationDTO.getLastName())
                    .email(registrationDTO.getEmail())
                    .phone(registrationDTO.getPhoneNumber())
                    .password(registrationDTO.getPassword())
                    .isGuest(false)
                    .build();
                log.info("Registering new customer with email: {}", registrationDTO.getEmail());
                return saveCustomer(customer);
            });
    }

    @Override
    @Transactional
    public Customer registerGuestCustomer(String email, String firstName, String lastName, String phone) {
        return customerRepository.findByEmail(email)
            .orElseGet(() -> {
                Customer guest = Customer.createGuest(email, firstName, lastName, phone);
                log.info("Creating new guest customer with email: {}", email);
                return saveCustomer(guest);
            });
    }

    @Override
    @Transactional
    public Customer convertGuestToRegistered(String email, CustomerRegistrationDTO registrationDTO) {
        Customer guest = customerRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("Guest customer not found"));

        if (!guest.isGuest()) {
            throw new IllegalStateException("Customer is already registered");
        }

        // Check if the new email is already in use by another registered user
        customerRepository.findByEmail(registrationDTO.getEmail())
            .ifPresent(existing -> {
                if (!existing.getId().equals(guest.getId()) && !existing.isGuest()) {
                    throw new IllegalStateException("Email already in use by another registered user");
                }
            });

        // Update guest to registered user
        guest.convertToRegisteredUser(registrationDTO.getPassword())
            .setFirstName(registrationDTO.getFirstName())
            .setLastName(registrationDTO.getLastName())
            .setPhone(registrationDTO.getPhoneNumber())
            .setEmail(registrationDTO.getEmail());

        log.info("Converting guest to registered user: {}", email);
        return saveCustomer(guest);
    }

    @Override
    public Customer loginCustomer(CustomerLoginDTO loginDTO) {
        log.info("Attempting login for email: {}", loginDTO.getEmail());
        
        Customer customer = customerRepository.findByEmail(loginDTO.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        // Verify password
        if (!passwordEncoder.matches(loginDTO.getPassword(), customer.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        if (!customer.isEnabled()) {
            throw new IllegalStateException("Account is disabled");
        }

        log.info("Login successful for customer: {}", customer.getEmail());
        return customer;
    }
}

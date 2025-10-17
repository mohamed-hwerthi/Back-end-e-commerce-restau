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
        return findExistingCustomer(customerDTO)
                .orElseGet(() -> registerGuestCustomer(
                        customerDTO.getEmail(),
                        customerDTO.getFirstName(),
                        customerDTO.getLastName(),
                        customerDTO.getPhone()
                ));
    }

    private Optional<Customer> findExistingCustomer(ClientCustomerDTO customerDTO) {
        return findById(customerDTO.getId())
                .or(() -> findByEmail(customerDTO.getEmail()))
                .or(() -> findByPhone(customerDTO.getPhone()));
    }


    @Override
    @Transactional
    public Customer registerCustomer(CustomerRegistrationDTO registrationDTO) {
        validateUniqueCustomer(registrationDTO);

        return customerRepository.findByEmail(registrationDTO.getEmail())
                .map(guest -> {
                    guest.convertToRegisteredUser(registrationDTO.getPassword());
                    guest.setFirstName(registrationDTO.getFirstName());
                    guest.setLastName(registrationDTO.getLastName());
                    guest.setPhone(registrationDTO.getPhoneNumber());
                    guest.setEmail(registrationDTO.getEmail()); 
                    log.info("Converted guest to registered customer: {}", registrationDTO.getEmail());
                    return saveCustomer(guest);
                })
                .orElseGet(() -> {
                    Customer newCustomer = Customer.builder()
                            .firstName(registrationDTO.getFirstName())
                            .lastName(registrationDTO.getLastName())
                            .email(registrationDTO.getEmail())
                            .phone(registrationDTO.getPhoneNumber())
                            .password(registrationDTO.getPassword())
                            .isGuest(false)
                            .build();
                    log.info("Registered new customer: {}", registrationDTO.getEmail());
                    return saveCustomer(newCustomer);
                });
    }

    private void validateUniqueCustomer(CustomerRegistrationDTO registrationDTO) {
        customerRepository.findByEmail(registrationDTO.getEmail()).ifPresent(customer -> {
            if (!customer.isGuest()) {
                throw new IllegalStateException("Email already in use");
            }
        });

        customerRepository.findByPhone(registrationDTO.getPhoneNumber()).ifPresent(customer -> {
            if (!customer.isGuest()) {
                throw new IllegalStateException("Phone number already in use");
            }
        });
    }

    // ------------------------------------------------------
    // 🔹 GUEST CUSTOMER HANDLING
    // ------------------------------------------------------

    @Override
    @Transactional
    public Customer registerGuestCustomer(String email, String firstName, String lastName, String phone) {
        return customerRepository.findByEmail(email)
                .orElseGet(() -> {
                    Customer guest = Customer.createGuest(email, firstName, lastName, phone);
                    log.info("Created new guest customer: {}", email);
                    return saveCustomer(guest);
                });
    }

    @Override
    @Transactional
    public Customer convertGuestToRegistered(String email, CustomerRegistrationDTO registrationDTO) {
        Customer guest = customerRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Guest customer not found for email: " + email));

        if (!guest.isGuest()) {
            throw new IllegalStateException("Customer is already registered");
        }

        // Check if another registered user already has the same email
        customerRepository.findByEmail(registrationDTO.getEmail())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(guest.getId()) && !existing.isGuest()) {
                        throw new IllegalStateException("Email already in use by another registered user");
                    }
                });

        guest.convertToRegisteredUser(registrationDTO.getPassword());
        guest.setFirstName(registrationDTO.getFirstName());
        guest.setLastName(registrationDTO.getLastName());
        guest.setPhone(registrationDTO.getPhoneNumber());
        guest.setEmail(registrationDTO.getEmail());

        log.info("Converted guest to registered customer: {}", email);
        return saveCustomer(guest);
    }


    @Override
    public boolean existsById(UUID customerId) {
        return customerId != null && customerRepository.existsById(customerId);
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
        return customerRepository.findByEmail(email);
    }

    private Optional<Customer> findByPhone(String phone) {
        if (!StringUtils.hasText(phone)) return Optional.empty();
        return customerRepository.findByPhone(phone);
    }

    @Transactional
    private Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }
}

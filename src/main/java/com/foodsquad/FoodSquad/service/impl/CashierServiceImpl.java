package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.mapper.CashierMapper;
import com.foodsquad.FoodSquad.model.dto.CashierDTO;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.entity.Cashier;
import com.foodsquad.FoodSquad.repository.CashierRepository;
import com.foodsquad.FoodSquad.service.declaration.CashierService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CashierServiceImpl implements CashierService {

    private final CashierRepository cashierRepository;
    private final CashierMapper cashierMapper;

    @Override
    public PaginatedResponseDTO<CashierDTO> findAllCashiers(int page, int limit) {
        Pageable pageable = PageRequest.of(page, limit);
        Page<Cashier> cashiersPage = cashierRepository.findAll(pageable);
        
        List<CashierDTO> cashierDTOs = cashiersPage.getContent().stream()
                .map(cashierMapper::toDto)
                .toList();
                
        return new PaginatedResponseDTO<>(cashierDTOs, cashiersPage.getTotalElements());
    }

    @Override
    public List<CashierDTO> findAllCashiers() {
        return cashierRepository.findAll().stream()
                .map(cashierMapper::toDto)
                .toList();
    }

    @Override
    public CashierDTO findCashierById(String id) {
        Cashier cashier = cashierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cashier not found with id: " + id));
        return cashierMapper.toDto(cashier);
    }

    @Override
    public CashierDTO findCashierByCashierId(String cashierId) {
        Cashier cashier = cashierRepository.findByCashierId(cashierId)
                .orElseThrow(() -> new EntityNotFoundException("Cashier not found with cashier ID: " + cashierId));
        return cashierMapper.toDto(cashier);
    }

    @Override
    @Transactional
    public CashierDTO createCashier(CashierDTO cashierDTO) {
        if (cashierRepository.existsByCashierId(cashierDTO.getCashierId())) {
            throw new EntityNotFoundException("Cashier with ID " + cashierDTO.getCashierId() + " already exists");
        }
        Cashier cashier = cashierMapper.toEntity(cashierDTO);
        Cashier savedCashier = cashierRepository.save(cashier);
        return cashierMapper.toDto(savedCashier);
    }

    @Override
    @Transactional
    public CashierDTO updateCashier(String id, CashierDTO cashierDTO) {
        Cashier existingCashier = cashierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cashier not found with id: " + id));
        
        cashierMapper.updateCashierFromDto(cashierDTO, existingCashier);
        Cashier updatedCashier = cashierRepository.save(existingCashier);
        return cashierMapper.toDto(updatedCashier);
    }

    @Override
    @Transactional
    public void deleteCashier(String id) {
        if (!cashierRepository.existsById(id)) {
            throw new EntityNotFoundException("Cashier not found with id: " + id);
        }
        cashierRepository.deleteById(id);
    }

    @Override
    public Cashier findCashier(String id) {
        return cashierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cashier not found with id: " + id));
    }

    @Override
    public Cashier saveCashier(Cashier cashier) {
        return cashierRepository.save(cashier);
    }
}

package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.mapper.CashMovementMapper;
import com.foodsquad.FoodSquad.model.dto.PaginatedResponseDTO;
import com.foodsquad.FoodSquad.model.dto.cashmovement.CashMovementRequestDTO;
import com.foodsquad.FoodSquad.model.dto.cashmovement.CashMovementResponseDTO;
import com.foodsquad.FoodSquad.model.entity.CashMovement;
import com.foodsquad.FoodSquad.model.entity.CashierSession;
import com.foodsquad.FoodSquad.model.entity.User;
import com.foodsquad.FoodSquad.repository.CashMovementRepository;
import com.foodsquad.FoodSquad.service.CashierSessionService;
import com.foodsquad.FoodSquad.service.admin.dec.UserService;
import com.foodsquad.FoodSquad.service.dec.CashMovementService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CashMovementServiceImpl implements CashMovementService {

    private final CashMovementRepository cashMovementRepository;
    private final CashierSessionService cashierSessionService;
    private final UserService userService;
    private final CashMovementMapper cashMovementMapper;

    @Override
    @Transactional
    public CashMovementResponseDTO createCashMovement(CashMovementRequestDTO requestDTO) {
        log.info("Creating new cash movement: {}", requestDTO);

        CashierSession session = cashierSessionService.getSession(requestDTO.getCashierSessionId());
        User cashier = userService.getUserById(requestDTO.getCashierId());

        CashMovement movement = cashMovementMapper.toEntity(requestDTO);
        movement.setCashier(cashier);
        movement.setCashierSession(session);

        movement = cashMovementRepository.save(movement);

        log.info("Created cash movement with id: {}", movement.getId());
        return cashMovementMapper.toDto(movement);
    }

    @Override
    @Transactional(readOnly = true)
    public CashMovementResponseDTO getCashMovementById(UUID id) {
        log.info("Fetching cash movement with id: {}", id);
        return cashMovementRepository.findById(id)
                .map(cashMovementMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("CashMovement not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CashMovementResponseDTO> getCashMovementsBySession(UUID sessionId) {
        log.info("Fetching all cash movements for session: {}", sessionId);
        return cashMovementRepository.findByCashierSessionIdOrderByTimestampDesc(sessionId).stream()
                .map(cashMovementMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CashMovementResponseDTO> getCashMovementsByCashier(UUID cashierId) {
        log.info("Fetching all cash movements for cashier: {}", cashierId);
        return cashMovementRepository.findByCashierIdOrderByTimestampDesc(cashierId).stream()
                .map(cashMovementMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCashMovement(UUID id) {
        log.info("Deleting cash movement with id: {}", id);
        if (!cashMovementRepository.existsById(id)) {
            throw new EntityNotFoundException("CashMovement not found with id: " + id);
        }
        cashMovementRepository.deleteById(id);
        log.info("Deleted cash movement with id: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedResponseDTO<CashMovementResponseDTO> getAllCashMovements(Pageable pageable) {
        log.info("Fetching all cash movements with pagination");
        Page<CashMovement> page = cashMovementRepository.findAll(pageable);

        List<CashMovementResponseDTO> content = page.getContent().stream()
                .map(cashMovementMapper::toDto)
                .collect(Collectors.toList());

        return new PaginatedResponseDTO<>(content, page.getTotalElements());
    }
}

package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.mapper.CashierSessionMapper;
import com.foodsquad.FoodSquad.model.dto.cashier.CashierSessionRequestDTO;
import com.foodsquad.FoodSquad.model.dto.cashier.CashierSessionResponseDTO;
import com.foodsquad.FoodSquad.model.entity.CashierSession;
import com.foodsquad.FoodSquad.model.entity.User;
import com.foodsquad.FoodSquad.repository.CashierSessionRepository;
import com.foodsquad.FoodSquad.repository.StoreRepository;
import com.foodsquad.FoodSquad.service.CashierSessionService;
import com.foodsquad.FoodSquad.service.admin.dec.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CashierSessionServiceImpl implements CashierSessionService {

    private final CashierSessionRepository cashierSessionRepository;
    private final StoreRepository storeRepository;
    private final CashierSessionMapper cashierSessionMapper;
    private final UserService userService;

    @Override
    @Transactional
    public CashierSessionResponseDTO createSession(CashierSessionRequestDTO requestDTO) {
        User cashier = userService.findById(requestDTO.getCashierId());
        CashierSession session = cashierSessionMapper.toEntity(requestDTO);
        session.setSessionNumber(generateSessionNumber());
        session.setStartTime(LocalDateTime.now());
        session.setIsClosed(false);
        session.setCashier(cashier);
        CashierSession savedSession = cashierSessionRepository.save(session);
        return cashierSessionMapper.toDto(savedSession);
    }

    @Override
    @Transactional
    public CashierSessionResponseDTO updateSession(UUID id, CashierSessionRequestDTO requestDTO) {
        CashierSession session = cashierSessionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cashier session not found with id: " + id));

        if (Boolean.TRUE.equals(session.getIsClosed())) {
            throw new IllegalStateException("Cannot update a closed cashier session");
        }

        cashierSessionMapper.updateEntityFromDto(requestDTO, session);

        if (!session.getCashier().getId().equals(requestDTO.getCashierId())) {
            User cashier = userService.findById(requestDTO.getCashierId());
            session.setCashier(cashier);
        }

        CashierSession updatedSession = cashierSessionRepository.save(session);
        return cashierSessionMapper.toDto(updatedSession);
    }

    @Override
    @Transactional
    public CashierSessionResponseDTO closeSession(UUID id, Double closingBalance) {
        CashierSession session = cashierSessionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cashier session not found with id: " + id));

        if (Boolean.TRUE.equals(session.getIsClosed())) {
            throw new IllegalStateException("Session is already closed");
        }

        session.setClosingBalance(closingBalance);
        session.setEndTime(LocalDateTime.now());
        session.setIsClosed(true);

        CashierSession closedSession = cashierSessionRepository.save(session);
        return cashierSessionMapper.toDto(closedSession);
    }

    @Override
    public CashierSessionResponseDTO getSessionById(UUID id) {
        CashierSession session = cashierSessionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cashier session not found with id: " + id));
        return cashierSessionMapper.toDto(session);
    }

    @Override
    public List<CashierSessionResponseDTO> getAllSessions() {
        return cashierSessionRepository.findAll().stream()
                .map(cashierSessionMapper::toDto)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public List<CashierSessionResponseDTO> getSessionsByCashier(UUID cashierId) {
        return cashierSessionRepository.findByCashierId(cashierId).stream()
                .map(cashierSessionMapper::toDto)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public CashierSessionResponseDTO getActiveCashierSession(UUID cashierId) {
        return cashierSessionRepository.findByCashierIdAndIsClosed(cashierId, false)
                .stream()
                .findFirst()
                .map(cashierSessionMapper::toDto)
                .orElse(null);
    }

    @Override
    public List<CashierSessionResponseDTO> getSessionsByStatus(Boolean isClosed) {
        return cashierSessionRepository.findByIsClosed(isClosed).stream()
                .map(cashierSessionMapper::toDto)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteSession(UUID id) {
        CashierSession session = cashierSessionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cashier session not found with id: " + id));

        if (!session.getOrders().isEmpty() || !session.getCashMovements().isEmpty()) {
            throw new IllegalStateException("Cannot delete session with associated orders or cash movements");
        }

        cashierSessionRepository.delete(session);
    }

    @Override
    public CashierSession getSession(UUID id) {
        return cashierSessionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cashier session not found with id: " + id));
    }

    private String generateSessionNumber() {
        return "SESS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}

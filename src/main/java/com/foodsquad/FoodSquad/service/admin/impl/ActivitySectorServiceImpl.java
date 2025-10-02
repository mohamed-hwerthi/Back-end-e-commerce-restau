package com.foodsquad.FoodSquad.service.admin.impl;

import com.foodsquad.FoodSquad.mapper.ActivitySectorMapper;
import com.foodsquad.FoodSquad.model.dto.ActivitySectorDTO;
import com.foodsquad.FoodSquad.model.entity.ActivitySector;
import com.foodsquad.FoodSquad.repository.ActivitySectorRepository;
import com.foodsquad.FoodSquad.service.admin.dec.ActivitySectorService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivitySectorServiceImpl implements ActivitySectorService {

    private final ActivitySectorRepository activitySectorRepository;
    private final ActivitySectorMapper activitySectorMapper;
    private final DataSource dataSource;

    @Override
    @Transactional
    public ActivitySectorDTO save(ActivitySectorDTO activitySectorDTO) {
        log.info("Saving new ActivitySector: {}", activitySectorDTO);
        logCurrentSchema();
        ActivitySector activitySector = activitySectorMapper.toEntity(activitySectorDTO);
        ActivitySector saved = activitySectorRepository.save(activitySector);
        ActivitySectorDTO result = activitySectorMapper.toDto(saved);
        log.info("Saved ActivitySector with id: {}", result.getId());
        return result;
    }

    @Override
    @Transactional
    public ActivitySectorDTO update(UUID id, ActivitySectorDTO activitySectorDTO) {
        log.info("Updating ActivitySector with id: {}", id);

        return activitySectorRepository.findById(id)
                .map(existing -> {
                    ActivitySector updated = activitySectorMapper.toEntity(activitySectorDTO);
                    updated.setId(existing.getId());
                    ActivitySector saved = activitySectorRepository.save(updated);
                    log.info("Updated ActivitySector with id: {}", id);
                    return activitySectorMapper.toDto(saved);
                })
                .orElseThrow(() -> {
                    log.warn("Cannot update, ActivitySector not found with id: {}", id);
                    return new EntityNotFoundException("ActivitySector not found with id: " + id);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<ActivitySectorDTO> findAll() {
        log.info("Fetching all ActivitySectors");
        logCurrentSchema();
        List<ActivitySectorDTO> result = activitySectorRepository.findAll()
                .stream()
                .map(activitySectorMapper::toDto)
                .toList();
        log.info("Found {} ActivitySectors", result.size());
        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public ActivitySectorDTO findById(UUID id) {
        log.info("Fetching ActivitySector with id: {}", id);
        return activitySectorRepository.findById(id)
                .map(activitySectorMapper::toDto)
                .orElseThrow(() -> {
                    log.warn("ActivitySector not found with id: {}", id);
                    return new EntityNotFoundException("ActivitySector not found with id: " + id);
                });
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        log.info("Deleting ActivitySector with id: {}", id);
        if (!activitySectorRepository.existsById(id)) {
            log.warn("Cannot delete, ActivitySector not found with id: {}", id);
            throw new EntityNotFoundException("ActivitySector not found with id: " + id);
        }
        activitySectorRepository.deleteById(id);
        log.info("Deleted ActivitySector with id: {}", id);
    }


    private void logCurrentSchema() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SHOW search_path")) {

            if (rs.next()) {
                String schema = rs.getString(1);
                log.info("Current database schema (search_path): {}", schema);
            }
        } catch (Exception e) {
            log.error("Failed to get current schema", e);
        }
    }
}

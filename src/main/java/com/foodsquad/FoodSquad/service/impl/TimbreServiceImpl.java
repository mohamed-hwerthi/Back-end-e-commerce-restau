package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.mapper.TimbreMapper;
import com.foodsquad.FoodSquad.model.dto.TimbreDTO;
import com.foodsquad.FoodSquad.model.entity.Timbre;
import com.foodsquad.FoodSquad.repository.TimbreRepository;
import com.foodsquad.FoodSquad.service.declaration.TimbreService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TimbreServiceImpl implements TimbreService {

    private static final Logger logger = LoggerFactory.getLogger(TimbreServiceImpl.class);

    private final TimbreRepository timbreRepository;

    private final TimbreMapper timbreMapper;

    private final DataSource dataSource;

    @Override
    public TimbreDTO save(TimbreDTO timbreDTO) {

        logger.info("Saving new Timbre: {}", timbreDTO);
        Timbre timbre = timbreMapper.toEntity(timbreDTO);
        Timbre saved = timbreRepository.save(timbre);
        TimbreDTO result = timbreMapper.toDto(saved);
        logger.info("Saved Timbre with id: {}", result.getId());
        return result;
    }

    @Override
    public List<TimbreDTO> findAll() {

        logger.info("Fetching all Timbres");
        logCurrentSchema();
        List<TimbreDTO> result = timbreRepository.findAll()
                .stream()
                .map(timbreMapper::toDto)
                .toList();
        logger.info("Found {} Timbres", result.size());
        return result;
    }

    @Override
    public TimbreDTO findById(UUID id) {

        logger.info("Finding Timbre by id: {}", id);
        TimbreDTO result = timbreRepository.findById(id)
                .map(timbreMapper::toDto)
                .orElse(null);
        if (result != null) {
            logger.info("Found Timbre: {}", result);
        } else {
            logger.warn("No Timbre found with id: {}", id);
        }
        return result;
    }

    @Override
    public void delete(UUID id) {

        logger.info("Deleting Timbre with id: {}", id);
        timbreRepository.deleteById(id);
        logger.info("Deleted Timbre with id: {}", id);
    }

    @Override
    public TimbreDTO update(UUID timberId, TimbreDTO timbreDTO) {

        logger.info("Updating Timbre with id: {}", timberId);
      return timbreRepository.findById(timberId)
                .map(existing -> {
                    Timbre updated = timbreMapper.toEntity(timbreDTO);
                    updated.setId(existing.getId());
                    Timbre saved = timbreRepository.save(updated);
                    logger.info("Updated Timbre with id: {}", timberId);
                    return timbreMapper.toDto(saved);
                })
                .orElseGet(() -> {
                    logger.warn("Cannot update, Timbre not found with id: {}", timberId);
                    return null;
                });
    }


    private void logCurrentSchema() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SHOW search_path")) {

            if (rs.next()) {
                String schema = rs.getString(1);
                logger.info("Current database schema (search_path): {}", schema);
            }
        } catch (Exception e) {
            logger.error("Failed to get current schema", e);
        }
    }


}

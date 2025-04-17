package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.mapper.TimbreMapper;
import com.foodsquad.FoodSquad.model.dto.TimbreDTO;
import com.foodsquad.FoodSquad.model.entity.Timbre;
import com.foodsquad.FoodSquad.repository.TimbreRepository;
import com.foodsquad.FoodSquad.service.declaration.TimbreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimbreServiceImpl implements TimbreService {

    private final TimbreRepository timbreRepository;
    private final TimbreMapper timbreMapper;

    @Override
    public TimbreDTO save(TimbreDTO timbreDTO) {
        Timbre timbre = timbreMapper.toEntity(timbreDTO);
        timbre = timbreRepository.save(timbre);
        return timbreMapper.toDto(timbre);
    }

    @Override
    public List<TimbreDTO> findAll() {
        return timbreRepository.findAll()
                .stream()
                .map(timbreMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TimbreDTO findById(String id) {
        return timbreRepository.findById(id)
                .map(timbreMapper::toDto)
                .orElse(null);
    }

    @Override
    public void delete(String id) {
        timbreRepository.deleteById(id);
    }
    @Override
    public TimbreDTO update(String timberid  , TimbreDTO timbreDTO) {
        return timbreRepository.findById(timberid)
                .map(existing -> {
                    Timbre updated = timbreMapper.toEntity(timbreDTO);
                    updated.setId(existing.getId());
                    Timbre saved = timbreRepository.save(updated);
                    return timbreMapper.toDto(saved);
                })
                .orElse(null);
    }
}

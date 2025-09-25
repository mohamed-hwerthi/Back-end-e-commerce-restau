package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.dto.SupplementOptionDTO;
import com.foodsquad.FoodSquad.mapper.SupplementOptionMapper;
import com.foodsquad.FoodSquad.repository.SupplementOptionRepository;
import com.foodsquad.FoodSquad.service.SupplementOptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@slf4j
@RequiredArgsConstructor
public class SupplementOptionServiceImpl implements SupplementOptionService {


    private final SupplementOptionRepository supplementOptionRepository;

    private final SupplementOptionMapper supplementOptionMapper;

    @Override
    public List<SupplementOptionDTO> getAllSupplementOptions() {
        logger.info("Fetching all supplement options from the database");
        return supplementOptionMapper.toDTOs(supplementOptionRepository.findAll());
    }

    @Override
    public SupplementOptionDTO createSupplementOption(SupplementOptionDTO supplementOptionDTO) {
        logger.info("Creating a new supplement option: {}", supplementOptionDTO);
        SupplementOption supplementOption = supplementOptionMapper.toEntity(supplementOptionDTO);
        SupplementOption savedOption = supplementOptionRepository.save(supplementOption);
        return supplementOptionMapper.toDTO(savedOption);
    }

    @Override
    public void deleteSupplementOption(UUID id) {
        logger.info("Deleting supplement option with ID: {}", id);
        supplementOptionRepository.deleteById(id);
    }
}

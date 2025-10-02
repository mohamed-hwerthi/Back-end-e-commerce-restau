package com.foodsquad.FoodSquad.service.admin.impl;

import com.foodsquad.FoodSquad.mapper.LanguageMapper;
import com.foodsquad.FoodSquad.mapper.LocalizedStringMapper;
import com.foodsquad.FoodSquad.model.dto.LanguageDTO;
import com.foodsquad.FoodSquad.repository.LanguageRepository;
import com.foodsquad.FoodSquad.service.LanguageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;
    private final LanguageMapper languageMapper;
    private final LocalizedStringMapper localizedStringMapper;

    @Override
    @Transactional(readOnly = true)
    public List<LanguageDTO> findAll() {
        log.debug("Fetching all languages from repository");

        List<LanguageDTO> languages = languageRepository.findAll().stream()
                .map(language -> languageMapper.toDto(language, localizedStringMapper))
                .toList();

        log.info("Found {} languages", languages.size());

        return languages;
    }
}

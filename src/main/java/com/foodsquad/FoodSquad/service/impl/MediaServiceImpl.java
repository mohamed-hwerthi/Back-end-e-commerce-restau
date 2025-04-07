package com.foodsquad.FoodSquad.service.impl;

import com.foodsquad.FoodSquad.exception.FileUploadingException;
import com.foodsquad.FoodSquad.mapper.MediaMapper;
import com.foodsquad.FoodSquad.model.dto.MediaDTO;
import com.foodsquad.FoodSquad.model.entity.Media;
import com.foodsquad.FoodSquad.repository.MediaRepository;
import com.foodsquad.FoodSquad.service.declaration.MediaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service implementation for managing media items.
 * Provides methods to save, find, list, and delete media items.
 */
@Service
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;

    private final MediaMapper mediaMapper;

    private static final Logger logger = LoggerFactory.getLogger(MediaServiceImpl.class);

    @Value("${file.images-folder-path}")
    private String imagesFolderPath;

    /**
     * Constructor to initialize MediaServiceImpl with the necessary dependencies.
     *
     * @param mediaRepository the repository responsible for media entity operations
     * @param mediaMapper     the mapper for converting between Media entity and DTO
     */
    public MediaServiceImpl(MediaRepository mediaRepository, MediaMapper mediaMapper) {

        this.mediaRepository = mediaRepository;
        this.mediaMapper = mediaMapper;
    }

    /**
     * Saves a new media item.
     *
     * @param mediaDTO the media data to be saved
     * @return the saved media as a DTO
     */
    @Override
    public MediaDTO saveMedia(MediaDTO mediaDTO) {

        logger.debug("Saving media: {}", mediaDTO);
        Media media = mediaMapper.toEntity(mediaDTO);
        media = mediaRepository.save(media);
        MediaDTO savedMediaDTO = mediaMapper.toDto(media);
        return savedMediaDTO;
    }

    /**
     * Retrieves a media item by its ID.
     *
     * @param id the ID of the media item to find
     * @return the media item as a DTO
     * @throws EntityNotFoundException if the media item is not found
     */
    @Override
    public MediaDTO findMediaById(Long id) {

        logger.debug("Fetching media with id: {}", id);
        return mediaRepository.findById(id).map(mediaMapper::toDto).orElseThrow(() -> {
            return new EntityNotFoundException("Media with id " + id + " not found");
        });
    }

    /**
     * Retrieves all media items.
     *
     * @return a list of all media items as DTOs
     */
    @Override
    public List<MediaDTO> findAllMedia() {
        logger.debug("Fetching all media items");
       return mediaRepository.findAll().stream().map(mediaMapper::toDto).toList();
    }

    /**
     * Deletes a media item by its ID.
     *
     * @param id the ID of the media item to delete
     * @throws EntityNotFoundException if the media item with the given ID does not exist
     */
    @Override
    public void deleteMedia(Long id) {

        logger.debug("Deleting media with id: {}", id);
        if (!mediaRepository.existsById(id)) {
            throw new EntityNotFoundException("Media with id " + id + " not found");
        }
        mediaRepository.deleteById(id);
    }

    @Override
    public MediaDTO uploadFile(MultipartFile file) {

        try {
            String filePath = saveFile(file);
            Media media = new Media();
            media.setName(file.getOriginalFilename());
            media.setPath(filePath);
            media.setUrl(filePath);
            media.setType(file.getContentType());
            Media savedMedia = this.mediaRepository.save(media);
            return this.mediaMapper.toDto(savedMedia);
        } catch (IOException e) {
            throw new FileUploadingException("Error when uploading file ");

        }

    }

    private String saveFile(  @NotBlank(message = "file cannot be blank") MultipartFile file) throws IOException {

        Path uploadPath = Paths.get(imagesFolderPath);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        String fileName = file.getOriginalFilename();
        Path filePath = Optional.ofNullable(fileName).map(f -> uploadPath.resolve(fileName)).orElseThrow(()->new FileUploadingException("file name cannot be null"));

        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toString();
    }


}

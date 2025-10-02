package com.foodsquad.FoodSquad.service.admin.dec;

import com.foodsquad.FoodSquad.model.dto.MediaDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface MediaService {

    MediaDTO saveMedia(MediaDTO mediaDTO);

    MediaDTO findMediaById(UUID id);

    List<MediaDTO> findAllMedia();

    void deleteMedia(UUID id);

    MediaDTO uploadFile(MultipartFile file) throws Exception;

    List<MediaDTO> uploadMultipleFiles(MultipartFile[] files) throws Exception;


}
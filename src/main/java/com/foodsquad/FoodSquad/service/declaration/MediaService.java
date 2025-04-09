package com.foodsquad.FoodSquad.service.declaration;

import com.foodsquad.FoodSquad.model.dto.MediaDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MediaService {

    MediaDTO saveMedia(MediaDTO mediaDTO);

    MediaDTO findMediaById(Long id);

    List<MediaDTO> findAllMedia();

    void deleteMedia(Long id);

    MediaDTO uploadFile(MultipartFile file) throws Exception;


}
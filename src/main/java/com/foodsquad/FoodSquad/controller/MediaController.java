package com.foodsquad.FoodSquad.controller;

import com.foodsquad.FoodSquad.model.dto.CustomResponseDTO;
import com.foodsquad.FoodSquad.model.dto.MediaDTO;
import com.foodsquad.FoodSquad.service.declaration.MediaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * Controller for managing media resources.
 * This API provides endpoints to create, retrieve, and delete media items.
 */
@RestController
@RequestMapping("/api/media")
@Tag(name = "10. Media Management", description = "Media Management API")
@Slf4j
public class MediaController {

    private final MediaService mediaService;

    /**
     * Constructor to initialize the media service.
     *
     * @param mediaService the service responsible for media operations
     */
    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    /**
     * Creates a new media item.
     *
     * @param mediaDTO the media data transfer object containing media details
     * @return ResponseEntity containing the created MediaDTO
     */
    @PostMapping
    @Operation(summary = "Create a new media item", description = "This endpoint creates a new media item in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Media created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid media data")
    })
    public ResponseEntity<MediaDTO> createMedia(
            @Parameter(description = "Media object to be created", required = true) @RequestBody MediaDTO mediaDTO) {
        return ResponseEntity.ok(mediaService.saveMedia(mediaDTO));
    }

    /**
     * Retrieves a media item by its ID.
     *
     * @param id the unique identifier of the media item
     * @return ResponseEntity containing the found MediaDTO
     */
    @GetMapping("/{id}")
    @Operation(summary = "Find a media item by ID", description = "This endpoint retrieves a media item by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Media found"),
            @ApiResponse(responseCode = "404", description = "Media not found")
    })
    public ResponseEntity<MediaDTO> findMedia(
            @Parameter(description = "ID of the media item", required = true) @PathVariable UUID id) {
        return ResponseEntity.ok(mediaService.findMediaById(id));
    }

    /**
     * Retrieves all media items.
     *
     * @return ResponseEntity containing a list of MediaDTOs
     */
    @GetMapping
    @Operation(summary = "Find all media items", description = "This endpoint retrieves all media items in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of all media items"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<MediaDTO>> findAllMedia() {
        return ResponseEntity.ok(mediaService.findAllMedia());
    }

    /**
     * Deletes a media item by its ID.
     *
     * @param id the unique identifier of the media item to be deleted
     * @return ResponseEntity indicating the result of the deletion
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a media item", description = "This endpoint deletes a media item by its unique ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Media deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Media not found")
    })
    public ResponseEntity<Void> deleteMedia(
            @Parameter(description = "ID of the media item to delete", required = true) @PathVariable UUID id) {
        mediaService.deleteMedia(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping(value = "/upload-multiple", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Upload multiple media files",
            description = "This endpoint allows uploading multiple media files (images, videos, etc.) at once."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Files uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid files or request"),
            @ApiResponse(responseCode = "500", description = "Internal server error while uploading files")
    })
    public ResponseEntity<List<MediaDTO>> uploadMultipleFiles(
            @Parameter(description = "Files to be uploaded", required = true)
            @RequestParam("files") MultipartFile[] files
    ) throws Exception {
        log.info("Received multiple files upload request: {} files", files.length);

        try {
            List<MediaDTO> uploadedMedia = mediaService.uploadMultipleFiles(files);
            log.info("Files uploaded successfully: {} items", uploadedMedia.size());
            return ResponseEntity.ok(uploadedMedia);
        } catch (Exception ex) {
            log.error("Error uploading multiple files", ex);
            throw ex;
        }
    }


    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Upload a media file",
            description = "This endpoint allows you to upload a media file (image, video, etc.) and returns the created MediaDTO."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description
                    = "File uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid file or request"),
            @ApiResponse(responseCode = "500", description = "Internal server error while uploading file")
    })
    public ResponseEntity<MediaDTO> uploadFile(
            @Parameter(
                    description = "File to be uploaded",
                    required = true
            )
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        log.info("Received file upload request: {}", file.getOriginalFilename());

        try {
            MediaDTO uploadedMedia = mediaService.uploadFile(file);
            log.info("File uploaded successfully: {}", uploadedMedia.getId());
            return ResponseEntity.ok(uploadedMedia);
        } catch (Exception ex) {
            log.error("Error uploading file: {}", file.getOriginalFilename(), ex);
            throw ex;
        }
    }

}

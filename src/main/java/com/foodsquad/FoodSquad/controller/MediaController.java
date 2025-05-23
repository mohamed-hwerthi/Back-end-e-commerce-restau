package com.foodsquad.FoodSquad.controller;

import com.foodsquad.FoodSquad.model.dto.CustomResponseDTO;
import com.foodsquad.FoodSquad.model.dto.MediaDTO;
import com.foodsquad.FoodSquad.service.declaration.MediaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Controller for managing media resources.
 * This API provides endpoints to create, retrieve, and delete media items.
 */
@RestController
@RequestMapping("/api/media")
@Tag(name = "10. Media Management", description = "Media Management API")
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
            @Parameter(description = "ID of the media item", required = true) @PathVariable Long id) {
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
            @Parameter(description = "ID of the media item to delete", required = true) @PathVariable Long id) {
        mediaService.deleteMedia(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping(value = "/upload" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MediaDTO> uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        return   ResponseEntity.ok( mediaService.uploadFile(file));
    }

}

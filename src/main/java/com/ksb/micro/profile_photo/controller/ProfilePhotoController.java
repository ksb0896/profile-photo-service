package com.ksb.micro.profile_photo.controller;

import com.ksb.micro.profile_photo.service.impl.ProfilePhotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/v1/banks/{bankId}/users/{userId}")
@Tag(name = "Photo", description = "Profile photo endpoints")
@RequiredArgsConstructor
public class ProfilePhotoController {

    private final ProfilePhotoService profilePhotoService;

    @GetMapping(value = "/photo")
    @Operation(summary = "Get profile photo by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile photo found"),
            @ApiResponse(responseCode = "404", description = "Profile photo not found")
    })
    public ResponseEntity<byte[]> getProfilePhotoByUserId(@PathVariable Long bankId, @PathVariable Long userId) {
        return profilePhotoService.getProfilePhotoByUserId(bankId, userId)
                .map(photo -> ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(photo.getContentType()))
                        .contentLength(photo.getPhotoData().length)
                        .body(photo.getPhotoData()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, value = "/photo")
    @Operation(summary = "Profile photo uploaded")
    @ApiResponse(responseCode = "201", description = "Profile photo uploaded successfully")
    public ResponseEntity<String> uploadProfilePhoto(@PathVariable Long bankId, @PathVariable Long userId, @RequestParam("file") MultipartFile file) throws IOException {

        validateMultipartFile(file);

        if (profilePhotoService.getProfilePhotoByUserId(bankId, userId).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Photo already exists! Try to update it.");
        }

        profilePhotoService.saveProfilePhoto(bankId, userId, file.getBytes(), file.getContentType());
        return ResponseEntity.status(HttpStatus.CREATED).body("Photo uploaded successfully.");
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, value = "/photo")
    @Operation(summary = "Profile photo updated for that ID")
    public ResponseEntity<String> updateProfilePhoto(@PathVariable Long bankId, @PathVariable Long userId, @RequestParam("file") MultipartFile file) throws IOException {

        validateMultipartFile(file);

        profilePhotoService.updateProfilePhoto(bankId, userId, file.getBytes(), file.getContentType());
        return ResponseEntity.ok("Photo updated successfully!!");
    }

    @DeleteMapping(value = "/photo")
    @Operation(summary = "Profile photo deleted for that ID")
    public ResponseEntity<Void> deleteProfilePhoto(@PathVariable Long bankId, @PathVariable Long userId) {
        if (profilePhotoService.getProfilePhotoByUserId(bankId, userId).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        profilePhotoService.deleteProfilePhoto(bankId, userId);
        return ResponseEntity.noContent().build();
    }

    // centralized validation for MultipartFile
    private void validateMultipartFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty!");
        }

        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();

        if (!MediaType.IMAGE_PNG_VALUE.equalsIgnoreCase(contentType) ||
                fileName == null || !fileName.toLowerCase().endsWith(".png")) {
            throw new IllegalArgumentException("Only .png files are allowed!");
        }
    }
}

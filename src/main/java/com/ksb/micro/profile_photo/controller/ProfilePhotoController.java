package com.ksb.micro.profile_photo.controller;

import com.ksb.micro.profile_photo.model.ProfilePhoto;
import com.ksb.micro.profile_photo.service.impl.ProfilePhotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/v1/banks/{bankId}/users/{userId}")
@Tag(name = "Photo", description = "Profile photo endpoints")
public class ProfilePhotoController {

    @Autowired
    private ProfilePhotoService profilePhotoService;

    @GetMapping(value = "/photo")
    @Operation(summary = "Get profile photo by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile photo found"),
            @ApiResponse(responseCode = "404", description = "Profile photo not found")
    })
    public ResponseEntity<byte[]> getProfilePhotoByUserId(@PathVariable Long bankId, @PathVariable Long userId){
        Optional<ProfilePhoto> photo = profilePhotoService.getProfilePhotoByUserId(bankId,userId);
        if(photo.isPresent()){
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(photo.get().getContentType()));
            headers.setContentLength(photo.get().getPhotoData().length);
            return new ResponseEntity<>(photo.get().getPhotoData(),headers, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, value = "/photo")
    @Operation(summary = "Profile photo uploaded")
    @ApiResponse(responseCode = "201", description = "Profile photo uploaded successfully")
    public ResponseEntity<String> uploadProfilePhoto(@PathVariable Long bankId,@PathVariable Long userId, @RequestParam("file") MultipartFile file) throws IOException{
        // Check if the content type is image/png
        if (!"image/png".equalsIgnoreCase(file.getContentType())) {
            return new ResponseEntity<>("Only .png files are allowed!", HttpStatus.BAD_REQUEST);
        }

        // Verify the extension
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".png")) {
            return new ResponseEntity<>("Invalid file extension. Only .png is allowed!", HttpStatus.BAD_REQUEST);
        }

        Optional<ProfilePhoto> existingPhoto = profilePhotoService.getProfilePhotoByUserId(bankId,userId);
        if(existingPhoto.isPresent()){
            return new ResponseEntity<>("Photo already exists!! Try to update it", HttpStatus.CONFLICT);
        }
        profilePhotoService.saveProfilePhoto(bankId,userId, file.getBytes(), file.getContentType());
        return ResponseEntity.status(HttpStatus.CREATED).body("Photo uploaded successfully.");
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, value = "/photo")
    @Operation(summary = "Profile photo updated for that ID")
    public ResponseEntity<String> updateProfilePhoto(@PathVariable Long bankId,@PathVariable Long userId, @RequestParam("file") MultipartFile file) throws IOException{
        ProfilePhoto updatePhoto = profilePhotoService.updateProfilePhoto(bankId,userId,file.getBytes(),file.getContentType());
        // Check if the content type is image/png
        if (!"image/png".equalsIgnoreCase(file.getContentType())) {
            return new ResponseEntity<>("Only .png files are allowed!", HttpStatus.BAD_REQUEST);
        }

        // Verify the extension
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".png")) {
            return new ResponseEntity<>("Invalid file extension. Only .png is allowed!", HttpStatus.BAD_REQUEST);
        }
        if(updatePhoto !=null){
            return ResponseEntity.ok("Photo updated successfully!!");
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping(value = "/photo")
    @Operation(summary = "Profile photo deleted for that ID")
    public ResponseEntity<Void> deleteProfilePhoto(@PathVariable Long bankId,@PathVariable Long userId){
        Optional<ProfilePhoto> photo = profilePhotoService.getProfilePhotoByUserId(bankId,userId);
        if(photo.isEmpty()){
            return ResponseEntity.notFound().build(); //for 404
        }
        profilePhotoService.deleteProfilePhoto(bankId,userId);
        return ResponseEntity.noContent().build(); //for 204 status code
    }
}

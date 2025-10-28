package com.ksb.micro.profile_photo.service.impl;

import com.ksb.micro.profile_photo.model.ProfilePhoto;
import com.ksb.micro.profile_photo.repository.ProfilePhotoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfilePhotoServiceImpl implements ProfilePhotoService{

    @Autowired
    private ProfilePhotoRepository profilePhotoRepository;

    @Override
    public Optional<ProfilePhoto> getProfilePhotoByUserId(Long bankId,Long userId) {
        return profilePhotoRepository.findByBankIdAndUserId(bankId,userId);
    }

    @Override
    public ProfilePhoto saveProfilePhoto(Long bankId,Long userId, byte[] photoData, String contentType) {
        ProfilePhoto photo = new ProfilePhoto();
        photo.setBankId(bankId);
        photo.setUserId(userId);
        photo.setPhotoData(photoData);
        photo.setContentType(contentType);
        return profilePhotoRepository.save(photo); //saving the photo
    }

    @Override
    public ProfilePhoto updateProfilePhoto(Long bankId,Long userId, byte[] photoData, String contentType) {
//        Optional<ProfilePhoto> existingPhoto = profilePhotoRepository.findByUserId(bankId,userId);
//        if (existingPhoto.isPresent()) {
//            ProfilePhoto photoToUpdate = existingPhoto.get();
//            photoToUpdate.setPhotoData(photoData);
//            photoToUpdate.setContentType(contentType);
//            return profilePhotoRepository.save(photoToUpdate); //update and save
//        }
//        return null; //if not present, return 'null' response
        // Find the existing photo using both IDs to ensure it's the correct one
        ProfilePhoto photo = profilePhotoRepository.findByBankIdAndUserId(bankId, userId)
                .orElse(new ProfilePhoto()); // If not found, create a new one (idempotent PUT)

        photo.setBankId(bankId); // Set/update bankId
        photo.setUserId(userId); // Set/update userId
        photo.setPhotoData(photoData);
        photo.setContentType(contentType);
        return profilePhotoRepository.save(photo);
    }

    @Override
    @Transactional //custom-named repository method is properly executed in a transaction in database(explicitly)
    public void deleteProfilePhoto(Long bankId,Long userId) {
        profilePhotoRepository.deleteByBankIdAndUserId(bankId,userId);
    }
}

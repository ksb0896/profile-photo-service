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
        if("!image/png".equalsIgnoreCase(contentType)){
            throw new IllegalArgumentException("Unsupported file type: " + contentType);
        }
        ProfilePhoto photo = new ProfilePhoto();
        photo.setBankId(bankId);
        photo.setUserId(userId);
        photo.setPhotoData(photoData);
        photo.setContentType(contentType);
        return profilePhotoRepository.save(photo); //saving the photo
    }

    @Override
    public ProfilePhoto updateProfilePhoto(Long bankId,Long userId, byte[] photoData, String contentType) {
        if("!image/png".equalsIgnoreCase(contentType)){
            throw new IllegalArgumentException("Unsupported file type: " + contentType);
        }
        ProfilePhoto photo = profilePhotoRepository.findByBankIdAndUserId(bankId, userId)
                .orElseThrow(() -> new RuntimeException("Profile photo not found. Please upload a photo first."));

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

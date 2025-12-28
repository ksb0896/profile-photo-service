package com.ksb.micro.profile_photo.service.impl;

import com.ksb.micro.profile_photo.model.ProfilePhoto;

import java.util.Optional;

public interface ProfilePhotoService {
    Optional<ProfilePhoto> getProfilePhotoByUserId(Long bankId,Long userId);
    ProfilePhoto saveProfilePhoto(Long bankId,Long userId, byte[] photoData, String contentType);
    ProfilePhoto updateProfilePhoto(Long bankId,Long userId, byte[] photoData, String contentType);
    void deleteProfilePhoto(Long bankId,Long userId);
}

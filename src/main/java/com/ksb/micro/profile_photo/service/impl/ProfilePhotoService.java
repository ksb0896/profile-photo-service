package com.ksb.micro.profile_photo.service.impl;

import com.ksb.micro.profile_photo.model.ProfilePhoto;

import java.util.Optional;

public interface ProfilePhotoService {
    Optional<ProfilePhoto> getProfilePhotoByUserId(Long userId);
    ProfilePhoto saveProfilePhoto(Long userId, byte[] photoData, String contentType);
    ProfilePhoto updateProfilePhoto(Long userId, byte[] photoData, String contentType);
    void deleteProfilePhoto(Long userId);
}

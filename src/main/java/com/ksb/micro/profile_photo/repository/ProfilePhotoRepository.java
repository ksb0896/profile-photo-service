package com.ksb.micro.profile_photo.repository;

import com.ksb.micro.profile_photo.model.ProfilePhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfilePhotoRepository extends JpaRepository<ProfilePhoto, Long> {
    Optional<ProfilePhoto> findByUserId(Long userId);
    void deleteByUserId(Long userId);
}

package com.ksb.micro.profile_photo.service.impl;

import com.ksb.micro.profile_photo.model.ProfilePhoto;
import com.ksb.micro.profile_photo.repository.ProfilePhotoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

@Service
public class ProfilePhotoServiceImpl implements ProfilePhotoService {

    @Autowired
    private ProfilePhotoRepository profilePhotoRepository;

    @Override
    public Optional<ProfilePhoto> getProfilePhotoByUserId(Long bankId, Long userId) {
        return profilePhotoRepository.findByBankIdAndUserId(bankId, userId);
    }

    @Override
    public ProfilePhoto saveProfilePhoto(Long bankId, Long userId, byte[] photoData, String contentType) {
        if ("!image/png".equalsIgnoreCase(contentType)) {
            throw new IllegalArgumentException("Unsupported file type: " + contentType);
        }

        // Additional photo validations
        validateImage(photoData);
        byte[] optimizedData = compressImage(photoData);

        ProfilePhoto photo = new ProfilePhoto();
        photo.setBankId(bankId);
        photo.setUserId(userId);
        photo.setPhotoData(optimizedData);
        photo.setContentType(contentType);
        return profilePhotoRepository.save(photo); //saving the photo
    }

    @Override
    public ProfilePhoto updateProfilePhoto(Long bankId, Long userId, byte[] photoData, String contentType) {
        if ("!image/png".equalsIgnoreCase(contentType)) {
            throw new IllegalArgumentException("Unsupported file type: " + contentType);
        }
        ProfilePhoto photo = profilePhotoRepository.findByBankIdAndUserId(bankId, userId)
                .orElseThrow(() -> new RuntimeException("Profile photo not found. Please upload a photo first."));

        // Additional photo validations
        validateImage(photoData);
        byte[] optimizedData = compressImage(photoData);

        photo.setBankId(bankId); // Set/update bankId
        photo.setUserId(userId); // Set/update userId
        photo.setPhotoData(optimizedData);
        photo.setContentType(contentType);
        return profilePhotoRepository.save(photo);
    }

    @Override
    @Transactional //custom-named repository method is properly executed in a transaction in database(explicitly)
    public void deleteProfilePhoto(Long bankId, Long userId) {
        profilePhotoRepository.deleteByBankIdAndUserId(bankId, userId);
    }

    // Additional photo validations
    private byte[] compressImage(byte[] data) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            BufferedImage originalImage = ImageIO.read(bais);

            int targetWidth = 800;
            int targetHeight = (int) (originalImage.getHeight() * (targetWidth / (double) originalImage.getWidth()));

            Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_FAST);
            BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);

            Graphics2D g2d = outputImage.createGraphics();
            g2d.drawImage(resultingImage, 0, 0, null);
            g2d.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(outputImage, "png", baos);
            return baos.toByteArray();
        } catch (IOException e) {
            return data;
        }
    }

    private void validateImage(byte[] data) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data)) {
            BufferedImage image = ImageIO.read(bais);
            if (image == null) {
                throw new IllegalArgumentException("Invalid image content. File is not a valid PNG.");
            }
            if (image.getWidth() < 10 || image.getHeight() < 10) {
                throw new IllegalArgumentException("Image dimensions are too small.");
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Error validating image content.");
        }
    }
}

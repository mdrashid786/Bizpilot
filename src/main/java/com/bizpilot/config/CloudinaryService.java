package com.bizpilot.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    // Naya overload — byte[] accept karta hai (compressed image data)
    public String upload(byte[] fileBytes, String folder) {
        try {
            Map uploadResult = cloudinary.uploader().upload(fileBytes, ObjectUtils.asMap(
                    "folder", "bizpilot/" + folder,
                    "resource_type", "image"
            ));
            return (String) uploadResult.get("secure_url");
        } catch (IOException e) {
            throw new RuntimeException("Cloudinary upload failed", e);
        }
    }

    // Delete ke liye — Cloudinary URL se public_id nikaal ke delete karo
    public void delete(String imageUrl) {
        try {
            String publicId = extractPublicId(imageUrl);
            if (publicId != null) {
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            }
        } catch (IOException ignored) {
            // best-effort delete, fail silently
        }
    }

    private String extractPublicId(String imageUrl) {
        // Cloudinary URL pattern: https://res.cloudinary.com/{cloud}/image/upload/v123456/bizpilot/logo/abc123.jpg
        try {
            String afterUpload = imageUrl.substring(imageUrl.indexOf("/upload/") + 8);
            // version prefix (v123456/) hatao agar hai
            if (afterUpload.matches("^v\\d+/.*")) {
                afterUpload = afterUpload.substring(afterUpload.indexOf("/") + 1);
            }
            // file extension hatao
            int dotIndex = afterUpload.lastIndexOf(".");
            return dotIndex > 0 ? afterUpload.substring(0, dotIndex) : afterUpload;
        } catch (Exception e) {
            return null;
        }
    }
}
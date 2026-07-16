package com.bizpilot.business.service;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${app.file.upload-dir}")
    private String uploadDir;

    private static final List<String> ALLOWED_TYPES =
            List.of("image/jpeg", "image/png", "image/webp");

    private static final long MAX_UPLOAD_SIZE = 1 * 1024 * 1024; // 1 MB (frontend se aane wali file)
    private static final long TARGET_SIZE = 200 * 1024;          // 200 KB (final saved size)

    public String storeLogo(MultipartFile file) {
        return storeCompressed(file, "logo", 512, 512);
    }

    public String storeCategoryItemImage(MultipartFile file) {
        return storeCompressed(file, "category-item", 800, 800);
    }

    public String storeCoverImage(MultipartFile file) {
        return storeCompressed(file, "cover", 1600, 500);
    }

    private String storeCompressed(MultipartFile file, String subFolder, int width, int height) {

        validate(file);

        try {
            byte[] compressedBytes = compressToTargetSize(file, width, height);

            Path folderPath = Paths.get(uploadDir, subFolder);
            Files.createDirectories(folderPath);

            String fileName = UUID.randomUUID() + ".jpg"; // hamesha jpg mein save (best compression ratio)
            Path targetPath = folderPath.resolve(fileName);

            Files.write(targetPath, compressedBytes);

            return "/uploads/business/" + subFolder + "/" + fileName;

        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }

    private void validate(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("Only JPG, PNG, WebP allowed");
        }
        if (file.getSize() > MAX_UPLOAD_SIZE) {
            throw new IllegalArgumentException("File size must be under 1MB");
        }
    }

    /**
     * Pehle dimensions resize karte hain (isse quality kam kiye bina size kaafi kam ho jata hai),
     * phir agar phir bhi 200KB se bada hai, to quality ko step-by-step (0.9 se 0.5 tak) kam karte hain
     * jab tak target size na mil jaye. Isse visual quality zyada tar maintain rehti hai.
     */
    private byte[] compressToTargetSize(MultipartFile file, int width, int height) throws IOException {

        float quality = 0.9f;
        byte[] result;

        do {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            Thumbnails.of(file.getInputStream())
                    .size(width, height)
                    .outputFormat("jpg")
                    .outputQuality(quality)
                    .toOutputStream(outputStream);

            result = outputStream.toByteArray();
            quality -= 0.1f;

        } while (result.length > TARGET_SIZE && quality > 0.3f);

        return result;
    }

    public void delete(String relativePath) {
        if (relativePath == null || relativePath.isBlank()) return;

        try {
            String fileName = relativePath.replace("/uploads/business/", "");
            Path filePath = Paths.get(uploadDir, fileName);
            Files.deleteIfExists(filePath);
        } catch (IOException ignored) {
        }
    }
}
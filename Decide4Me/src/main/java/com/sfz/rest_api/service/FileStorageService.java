package com.sfz.rest_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileStorageService {

//    @Value("${file.upload-dir}")
//    private String uploadDir;

    private final String fileServer;

    @Autowired
    public FileStorageService(@Value("${local.file_server}") String fileServer) {
        this.fileServer = fileServer;
    }

    public String storeFile(MultipartFile file, String uploadDir, String name) throws IOException {
        // Ensure the upload directory exists
        Path uploadPath = Paths.get(fileServer + uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Extract the file extension from the original file name
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // Add the file extension to the name
        String fileNameWithExtension = name + fileExtension;

        // Store the file
        Path filePath = uploadPath.resolve(fileNameWithExtension);
        Files.copy(file.getInputStream(), filePath);

        return uploadDir + fileNameWithExtension;
    }

    public void deleteFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);

        if (Files.exists(path)) {
            Files.delete(path);
        } else {
            throw new IOException("File not found: " + filePath);
        }
    }

    public String storeUserProfile(MultipartFile file, String userId) throws IOException {
        return storeFile(file,"profile/" , userId);
    }


    public String storePostImage(MultipartFile file, String userId) throws IOException {
        return storeFile(file,"post/" , userId);
    }

}


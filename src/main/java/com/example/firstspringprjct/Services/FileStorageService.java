package com.example.firstspringprjct.Services;

import com.example.firstspringprjct.Utils.FileExceptions.FileNotFoundException;
import com.example.firstspringprjct.Utils.FileExceptions.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path rootDirectory;

    private static final Map<String, String> EXTENSIONS = Map.of(
            "application/pdf", ".pdf",
            "image/png", ".png",
            "image/jpeg", ".jpg"
    );

    public FileStorageService(
            @Value("${app.upload.directory:uploads}") String directory
    ) {
        this.rootDirectory = Paths.get(directory)
                .toAbsolutePath()
                .normalize();

        try {
            Files.createDirectories(rootDirectory);
        } catch (IOException e) {
            throw new FileStorageException(
                    "Could not initialize file storage", e
            );
        }
    }

    public String store(MultipartFile file) {
        String contentType = file.getContentType();
        String extension = EXTENSIONS.get(contentType);

        if (extension == null) {
            throw new FileStorageException("Unsupported file type");
        }

        // Never use the original filename as the storage path.
        String storageKey = UUID.randomUUID() + extension;
        Path destination = resolveSafePath(storageKey);

        try {
            Files.copy(file.getInputStream(), destination);
            return storageKey;
        } catch (IOException e) {
            throw new FileStorageException(
                    "Could not save the uploaded file", e
            );
        }
    }

    public Resource loadAsResource(String storageKey) {
        Path filePath = resolveSafePath(storageKey);

        try {
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new FileNotFoundException(-1L);
            }

            return resource;
        } catch (IOException e) {
            throw new FileStorageException(
                    "Could not read the stored file", e
            );
        }
    }

    public void delete(String storageKey) {
        Path filePath = resolveSafePath(storageKey);

        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new FileStorageException(
                    "Could not delete the stored file", e
            );
        }
    }

    private Path resolveSafePath(String storageKey) {
        Path resolved = rootDirectory.resolve(storageKey)
                .normalize();

        if (!resolved.startsWith(rootDirectory)) {
            throw new FileStorageException("Invalid storage key");
        }

        return resolved;
    }
}

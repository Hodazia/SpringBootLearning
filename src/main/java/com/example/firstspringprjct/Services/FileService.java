package com.example.firstspringprjct.Services;

import com.example.firstspringprjct.DTO.FileMetadataRequest;
import com.example.firstspringprjct.DTO.FileResponse;
import com.example.firstspringprjct.DTO.file.FilePageResponse;
import com.example.firstspringprjct.Entity.FileMetadata;
import com.example.firstspringprjct.Entity.PageResponse;
import com.example.firstspringprjct.Repository.FileRepository;
import com.example.firstspringprjct.Utils.FileExceptions.FileNotFoundException;
import com.example.firstspringprjct.Utils.FileExceptions.InvalidFileException;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FileService {

    private final FileRepository repository;
    private final FileStorageService storageService;
    private final FileValidationService validationService;

    public FileService(
            FileRepository repository,
            FileStorageService storageService,
            FileValidationService validationService
    ) {
        this.repository = repository;
        this.storageService = storageService;
        this.validationService = validationService;
    }

    public FileResponse upload(MultipartFile file) {
        validationService.validate(file);

        String storageKey = storageService.store(file);

        try {
            FileMetadata metadata = new FileMetadata(
                    null,
                    sanitizeFileName(file.getOriginalFilename()),
                    file.getSize(),
                    file.getContentType(),
                    LocalDateTime.now(),
                    storageKey,
                    true
            );

            return toResponse(repository.save(metadata));

        } catch (RuntimeException e) {
            // Avoid leaving an orphaned file if metadata creation fails.
            storageService.delete(storageKey);
            throw e;
        }
    }

    public FileResponse createMetadata(FileMetadataRequest request) {
        validateMetadataRequest(request);

        FileMetadata metadata = new FileMetadata(
                null,
                request.getFileName().trim(),
                request.getSizeBytes(),
                request.getContentType().trim(),
                LocalDateTime.now(),
                null,
                false
        );

        return toResponse(repository.save(metadata));
    }

    public FilePageResponse<FileResponse> getFiles(
            String keyword,
            String contentType,
            int page,
            int size
    ) {
        if (page < 0) {
            throw new InvalidFileException("Page must be zero or greater");
        }

        if (size < 1 || size > 100) {
            throw new InvalidFileException(
                    "Page size must be between 1 and 100"
            );
        }

        List<FileMetadata> matching =
                repository.search(keyword, contentType);

        int total = matching.size();
        int fromIndex = (int) Math.min((long) page * size, total);
        int toIndex = Math.min(fromIndex + size, total);

        List<FileResponse> content = matching
                .subList(fromIndex, toIndex)
                .stream()
                .map(this::toResponse)
                .toList();

        int totalPages = (int) Math.ceil((double) total / size);

        return new FilePageResponse<>(
                content,
                page,
                size,
                total,
                totalPages,
                page + 1 < totalPages
        );
    }

    public FileResponse getById(Long id) {
        return toResponse(findMetadata(id));
    }

    public Resource download(Long id) {
        FileMetadata metadata = findMetadata(id);
        ensureLocallyStored(metadata);

        return storageService.loadAsResource(metadata.getStorageKey());
    }

    public FileResponse getDownloadMetadata(Long id) {
        return toResponse(findMetadata(id));
    }

    @Transactional
    public void delete(Long id) {
        FileMetadata metadata = findMetadata(id);

        // Delete bytes first. If storage deletion fails, keep metadata.
        if (metadata.isLocallyStored()) {
            storageService.delete(metadata.getStorageKey());
        }

        repository.deleteById(id);
    }

    private FileMetadata findMetadata(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new FileNotFoundException(id));
    }

    private void ensureLocallyStored(FileMetadata metadata) {
        if (!metadata.isLocallyStored()
                || metadata.getStorageKey() == null) {
            throw new InvalidFileException(
                    "This record has metadata only; no local file is stored"
            );
        }
    }

    private FileResponse toResponse(FileMetadata file) {
        String downloadUrl = file.isLocallyStored()
                ? "/api/files/" + file.getId() + "/download"
                : null;

        return new FileResponse(
                file.getId(),
                file.getFileName(),
                file.getSizeBytes(),
                file.getContentType(),
                file.getUploadedAt(),
                file.isLocallyStored(),
                downloadUrl
        );
    }

    private String sanitizeFileName(String originalName) {
        if (originalName == null || originalName.isBlank()) {
            return "uploaded-file";
        }

        // Strip any client-supplied directory path.
        String name = originalName.replace('\\', '/');
        name = name.substring(name.lastIndexOf('/') + 1).trim();

        if (name.isBlank() || name.length() > 255) {
            throw new InvalidFileException("Invalid file name");
        }

        return name;
    }

    private void validateMetadataRequest(FileMetadataRequest request) {
        if (request.getFileName() == null
                || request.getFileName().isBlank()
                || request.getFileName().length() > 255) {
            throw new InvalidFileException("Invalid file name");
        }

        if (request.getSizeBytes() < 0) {
            throw new InvalidFileException(
                    "File size cannot be negative"
            );
        }

        if (request.getContentType() == null
                || request.getContentType().isBlank()) {
            throw new InvalidFileException(
                    "Content type is required"
            );
        }
    }
}

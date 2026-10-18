package com.example.firstspringprjct.Controller;

import com.example.firstspringprjct.DTO.FileMetadataRequest;
import com.example.firstspringprjct.DTO.FileResponse;
import com.example.firstspringprjct.DTO.file.FilePageResponse;
import com.example.firstspringprjct.Services.FileService;
import com.example.firstspringprjct.Utils.FileExceptions.InvalidFileException;
import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    // 1. Upload actual file bytes.
    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<FileResponse> upload(
            @RequestParam("file") MultipartFile file
    ) {
        FileResponse response = fileService.upload(file);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // 2. Create a metadata-only record.
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FileResponse> createMetadata(
            @Valid @RequestBody FileMetadataRequest request
    ) {
        FileResponse response = fileService.createMetadata(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // 3. List files with optional filters and pagination.
    @GetMapping
    public ResponseEntity<FilePageResponse<FileResponse>> getFiles(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String contentType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                fileService.getFiles(keyword, contentType, page, size)
        );
    }

    // 4. Get metadata for one file.
    @GetMapping("/{id}")
    public ResponseEntity<FileResponse> getById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(fileService.getById(id));
    }

    // 5. Download the actual file.
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(
            @PathVariable Long id
    ) {
        FileResponse metadata = fileService.getDownloadMetadata(id);
        Resource resource = fileService.download(id);

        ContentDisposition disposition = ContentDisposition.attachment()
                .filename(metadata.fileName(), StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(metadata.contentType()))
                .contentLength(metadata.sizeBytes())
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        disposition.toString()
                )
                .header("X-Content-Type-Options", "nosniff")
                .body(resource);
    }

    // 6. Preview PDF/images in the browser.
    @GetMapping("/{id}/preview")
    public ResponseEntity<Resource> preview(
            @PathVariable Long id
    ) {
        FileResponse metadata = fileService.getDownloadMetadata(id);

        if (!metadata.contentType().equals("application/pdf")
                && !metadata.contentType().equals("image/png")
                && !metadata.contentType().equals("image/jpeg")) {
            throw new InvalidFileException(
                    "Preview is supported only for PDF and images"
            );
        }

        Resource resource = fileService.download(id);

        ContentDisposition disposition = ContentDisposition.inline()
                .filename(metadata.fileName(), StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(metadata.contentType()))
                .contentLength(metadata.sizeBytes())
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        disposition.toString()
                )
                .header("X-Content-Type-Options", "nosniff")
                .body(resource);
    }

    // 7. Delete both stored bytes and metadata.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        fileService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

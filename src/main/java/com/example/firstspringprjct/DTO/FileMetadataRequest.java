package com.example.firstspringprjct.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class FileMetadataRequest {

    @NotBlank(message = "File name is required")
    @Size(max=255, message = "File name cannot exceed 255 characters")
    private String fileName;

    @PositiveOrZero(message = "File size cannot be negative")
    private long sizeBytes;

    @NotBlank(message="Content type is required")
    private String contentType;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getSizeBytes() {
        return sizeBytes;
    }

    public void setSizeBytes(long sizeBytes) {
        this.sizeBytes = sizeBytes;
    }
}

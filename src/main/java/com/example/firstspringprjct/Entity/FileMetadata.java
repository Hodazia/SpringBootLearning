package com.example.firstspringprjct.Entity;

import java.time.LocalDateTime;

/*
* Notice that storageKey is different from fileName.
* We will save the actual file under a server-generated name, such as 8a23...-uuid.pdf,
*  rather than trusting the uploaded filename as a filesystem path.
The locallyStored flag lets us distinguish a real uploaded file from a metadata-only record
created through POST /api/files.
*
* */


public class FileMetadata {

    private Long id;
    private String fileName;
    private Long sizeBytes;
    private String contentType;
    private LocalDateTime uploadedAt;
    private String storageKey;
    private boolean locallyStored;

    public FileMetadata() {
    }

    public FileMetadata(
            Long id,
            String fileName,
            long sizeBytes,
            String contentType,
            LocalDateTime uploadedAt,
            String storageKey,
            boolean locallyStored
    ) {
        this.id = id;
        this.fileName = fileName;
        this.sizeBytes = sizeBytes;
        this.contentType = contentType;
        this.uploadedAt = uploadedAt;
        this.storageKey = storageKey;
        this.locallyStored = locallyStored;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getSizeBytes() {
        return sizeBytes;
    }

    public void setSizeBytes(long sizeBytes) {
        this.sizeBytes = sizeBytes;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public String getStorageKey() {
        return storageKey;
    }

    public void setStorageKey(String storageKey) {
        this.storageKey = storageKey;
    }

    public boolean isLocallyStored() {
        return locallyStored;
    }

    public void setLocallyStored(boolean locallyStored) {
        this.locallyStored = locallyStored;
    }
}

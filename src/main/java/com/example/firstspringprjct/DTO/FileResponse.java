package com.example.firstspringprjct.DTO;

import java.time.LocalDateTime;

public record FileResponse (
    Long id,
    String fileName,
    long sizeBytes,
    String contentType,
    LocalDateTime uploadedAt,
    boolean locallyStored,
    String downloadUrl
){

}

package com.example.firstspringprjct.DTO.file;

import java.util.List;

public record FilePageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext
)
{
}

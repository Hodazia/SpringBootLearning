package com.example.firstspringprjct.Entity;

import java.util.List;

public class PageResponse<T> {

    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean hasNext;

    public PageResponse(
            List<T> content,
            int page,
            int size,
            long totalElements,
            int totalPages,
            boolean hasNext) {
        this.content = content;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.hasNext = hasNext;
    }

    public List<T> getContent() {
        return content;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public int getSize() {
        return size;
    }

    public int getPage() {
        return page;
    }
}

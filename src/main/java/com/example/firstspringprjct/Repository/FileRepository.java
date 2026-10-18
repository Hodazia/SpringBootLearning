package com.example.firstspringprjct.Repository;

import com.example.firstspringprjct.Entity.FileMetadata;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

@Repository
public class FileRepository {
    private final ConcurrentMap<Long, FileMetadata> files =
            new ConcurrentHashMap<>();

    private final AtomicLong idGenerator = new AtomicLong(0);

    public FileMetadata save(FileMetadata file) {
        if (file.getId() == null) {
            file.setId(idGenerator.incrementAndGet());
        }

        files.put(file.getId(), file);
        return file;
    }

    public Optional<FileMetadata> findById(Long id) {
        return Optional.ofNullable(files.get(id));
    }

    public List<FileMetadata> findAll() {
        return List.copyOf(files.values());
    }

    public void deleteById(Long id) {
        files.remove(id);
    }

    public boolean existsById(Long id) {
        return files.containsKey(id);
    }

    public List<FileMetadata> search(
            String keyword,
            String contentType
    ) {
        Stream<FileMetadata> stream = files.values().stream();

        if (keyword != null && !keyword.isBlank()) {
            String normalizedKeyword = keyword.toLowerCase();

            stream = stream.filter(file ->
                    file.getFileName()
                            .toLowerCase()
                            .contains(normalizedKeyword)
            );
        }

        if (contentType != null && !contentType.isBlank()) {
            stream = stream.filter(file ->
                    file.getContentType().equalsIgnoreCase(contentType)
            );
        }

        return stream
                .sorted(Comparator.comparing(
                        FileMetadata::getUploadedAt,
                        Comparator.nullsLast(Comparator.reverseOrder())
                ))
                .toList();
    }
}

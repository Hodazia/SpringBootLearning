package com.example.firstspringprjct.Services;

import com.example.firstspringprjct.Utils.FileExceptions.InvalidFileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

@Service
public class FileValidationService {

    @Value("${app.upload.max-size-bytes}")
    private long maxSizeBytes;

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "application/pdf",
            "image/png",
            "image/jpeg"
    );

    @Value("${app.upload.allowed-types}")
    private List<String> allowedTypes;

    public void validate(MultipartFile file)
    {
        if (file == null || file.isEmpty()) {
            throw new InvalidFileException("Please upload a non-empty file");
        }

        if (file.getSize() > maxSizeBytes) {
            throw new InvalidFileException(
                    "File size exceeds the limit of "
                            + maxSizeBytes / (1024 * 1024) + " MB"
            );
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            throw new InvalidFileException(
                    "Only PDF, PNG and JPEG files are allowed"
            );
        }

        validateFileSignature(file, contentType);
    }

    private void validateFileSignature(MultipartFile file,String contentType)
    {
        try(InputStream input = file.getInputStream())
        {
            byte[] header = input.readNBytes(8);

            boolean valid = switch (contentType) {
                case "application/pdf" ->
                        startsWith(header, new byte[]{'%', 'P', 'D', 'F', '-'});

                case "image/png" ->
                        startsWith(header, new byte[]{
                                (byte) 0x89, 'P', 'N', 'G',
                                0x0D, 0x0A, 0x1A, 0x0A
                        });

                case "image/jpeg" ->
                        header.length >= 3
                                && (header[0] & 0xFF) == 0xFF
                                && (header[1] & 0xFF) == 0xD8
                                && (header[2] & 0xFF) == 0xFF;

                default -> false;
            };

            if (!valid) {
                throw new InvalidFileException(
                        "File contents do not match the declared content type"
                );
            }

        } catch (IOException e) {
            throw new InvalidFileException(
                    "Unable to read the uploaded file"
            );
        }

    }

    private boolean startsWith(byte[] actual, byte[] expected) {
        if (actual.length < expected.length) {
            return false;
        }

        for (int i = 0; i < expected.length; i++) {
            if (actual[i] != expected[i]) {
                return false;
            }
        }

        return true;
    }

}

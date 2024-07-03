package com.thoughts.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

@Service
@RequiredArgsConstructor
public class ImageService {

    @Value("${file.storage.location:temp}")
    private final String bucket;

    @SneakyThrows
    public void upload(String imagePath, InputStream stream) {
        Path fullPath = Path.of(bucket, imagePath);
        try (stream) {
            Files.createDirectories(fullPath.getParent());
            Files.write(fullPath, stream.readAllBytes(), CREATE, TRUNCATE_EXISTING)
        }
    }

    @SneakyThrows
    public Optional<byte[]> get(String imagePath) {
        Path fullImagePath = Path.of(bucket, imagePath);
        return Files.exists(fullImagePath)
                ? Optional.of(Files.readAllBytes(fullImagePath))
                : Optional.empty();
    }
}

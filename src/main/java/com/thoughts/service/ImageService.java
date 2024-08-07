package com.thoughts.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

@Service
public class ImageService {

    private final String bucket;

    public ImageService(@Value("${file.storage.location:temp}") String bucket) {
        this.bucket = bucket;
    }

    @SneakyThrows
    public void upload(String imagePath, InputStream stream) {
        Path fullPath = Path.of(bucket, imagePath);
        try (stream) {
            Files.createDirectories(fullPath.getParent());
            Files.write(fullPath, stream.readAllBytes(), CREATE, TRUNCATE_EXISTING);
        }
    }
}

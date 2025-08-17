package org.bingo.model;


import java.util.Map;
import java.util.HashMap;

import java.nio.file.Path;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;

import java.io.IOException;


public class ImageLoader {
    public static Map<String, Path> loadImagesAndLabels(Path resourceDir) throws IOException {
        if (!Files.isDirectory(resourceDir) || Files.notExists(resourceDir)) {
            throw new IOException("Resource directory does not exist: " + resourceDir);
        }

        Map<String, Path> images = new HashMap<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(resourceDir, "*.svg")) {
            for (Path path : stream) {
                String label = path.getFileName().toString().replaceFirst("[.][^.]+$", "");
                images.put(label, path);
            }
        }
        return images;
    }
}

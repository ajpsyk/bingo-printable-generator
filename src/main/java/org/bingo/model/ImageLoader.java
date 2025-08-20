package org.bingo.model;


import java.io.FileInputStream;
import java.util.Map;
import java.util.HashMap;
import com.itextpdf.svg.converter.SvgConverter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import java.nio.file.Path;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;

import java.io.IOException;


public class ImageLoader {
    public static Map<String, PdfFormXObject> loadImagesAndLabels(Path resourceDir) throws IOException {
        if (!Files.isDirectory(resourceDir) || Files.notExists(resourceDir)) {
            throw new IOException("Resource directory does not exist: " + resourceDir);
        }

        Map<String, PdfFormXObject> images = new HashMap<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(resourceDir, "*.svg")) {
            for (Path path : stream) {
                String label = path.getFileName().toString().replaceFirst("[.][^.]+$", "");
                try (FileInputStream stream = new FileInputStream(path.toFile())) {
                    PdfFormXObject image = SvgConverter.convertToXObject((stream))
                }
                images.put(label, path);
            }
        }
        return images;
    }
}

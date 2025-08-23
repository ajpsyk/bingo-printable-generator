package org.bingo.services;


import java.io.FileInputStream;
import java.util.Map;
import java.util.HashMap;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.svg.converter.SvgConverter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import org.bingo.model.BingoSquare;

import java.nio.file.Path;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;

import java.io.IOException;



public class ImageLoader {

    public static PdfFormXObject loadImage (Path resourcePath, PdfDocument document) throws IOException {
        PdfFormXObject image;
        try (FileInputStream stream = new FileInputStream(resourcePath.toFile())) {
            image = SvgConverter.convertToXObject(stream, document);
        }
        return image;
    }

    public static Map<String, BingoSquare> loadImageDirectory(Path resourceDir, PdfDocument document) throws IOException {
        if (!Files.isDirectory(resourceDir) || Files.notExists(resourceDir)) {
            throw new IOException("Resource directory does not exist: " + resourceDir);
        }

        Map<String, BingoSquare> bingoIcons = new HashMap<>();

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(resourceDir, "*.svg")) {
            for (Path path : directoryStream) {
                String label = path.getFileName().toString().replaceFirst("[.][^.]+$", "");
                if (bingoIcons.containsKey(label)) {
                    throw new IllegalArgumentException("Duplicate image label: " + label);
                }
                PdfFormXObject labelImage = loadLabel(label, document);
                PdfFormXObject image = loadImage(path, document);
                bingoIcons.put(label, new BingoSquare(labelImage, image));
            }
        }
        return bingoIcons;
    }

    private static PdfFormXObject loadLabel (String label, PdfDocument document) {
        return null;
    }
}

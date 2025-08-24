package org.bingo.services;


import java.io.FileInputStream;
import java.util.Map;
import java.util.HashMap;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.svg.converter.SvgConverter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import org.bingo.model.BingoSquare;

import java.nio.file.Path;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;

import java.io.IOException;



public class XObjectLoader {

    public static PdfFormXObject loadImage (Path resourcePath, PdfDocument document) throws IOException {
        PdfFormXObject image;
        try (FileInputStream stream = new FileInputStream(resourcePath.toFile())) {
            image = SvgConverter.convertToXObject(stream, document);
        }
        return image;
    }

    public static Map<String, BingoSquare> loadImageDirectory(Path resourceDir, PdfDocument document, PdfFont font) throws IOException {
        if (!Files.isDirectory(resourceDir) || Files.notExists(resourceDir)) {
            throw new IOException("Resource directory does not exist: " + resourceDir);
        }

        Map<String, BingoSquare> bingoIcons = new HashMap<>();

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(resourceDir, "*.svg")) {
            for (Path path : directoryStream) {
                String label = path.getFileName().toString().replaceFirst("[.][^.]+$", "").toUpperCase();
                if (bingoIcons.containsKey(label)) {
                    throw new IllegalArgumentException("Duplicate image label: " + label);
                }
                PdfFormXObject labelImage = loadLabel(label, document, font);
                PdfFormXObject image = loadImage(path, document);
                bingoIcons.put(label, new BingoSquare(labelImage, image));
            }
        }
        return bingoIcons;
    }

    private static PdfFormXObject loadLabel (String label, PdfDocument document, PdfFont font) {
        String strippedSpaceLabel = label.replace(" ", "");
        float labelWidth = font.getWidth(strippedSpaceLabel, 12);
        float labelHeight = font.getAscent(strippedSpaceLabel, 12) - font.getDescent(strippedSpaceLabel, 12);

        PdfFormXObject labelObject = new PdfFormXObject(new Rectangle(labelWidth, labelHeight));
        PdfCanvas canvas = new PdfCanvas(labelObject, document);
        canvas.beginText().setFontAndSize(font, 12).moveText(0,0).showText(label).endText();

        return labelObject;
    }
}

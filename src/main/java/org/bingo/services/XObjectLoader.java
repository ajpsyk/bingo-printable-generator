package org.bingo.services;


import java.util.Map;
import java.util.HashMap;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import org.bingo.model.BingoSquare;
import org.bingo.model.GridLayout;

import java.nio.file.Path;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;

import java.io.IOException;



public class XObjectLoader {

    public static PdfFormXObject loadImage (Path resourcePath, PdfDocument document) throws IOException {
        ImageData imageData = ImageDataFactory.create(resourcePath.toAbsolutePath().toString());
        PdfFormXObject image = new PdfFormXObject(new Rectangle(imageData.getWidth(), imageData.getHeight()));
        PdfCanvas canvas = new PdfCanvas(image, document);
        canvas.addImageAt(imageData, 0, 0, false);
        return image;
    }

    public static Map<String, BingoSquare> loadImageDirectory(Path resourceDir, PdfDocument document,GridLayout gl, PdfFont font, DeviceRgb color) throws IOException {
        if (!Files.isDirectory(resourceDir) || Files.notExists(resourceDir)) {
            throw new IOException("Resource directory does not exist: " + resourceDir);
        }

        Map<String, BingoSquare> bingoIcons = new HashMap<>();

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(resourceDir, "*.png")) {
            for (Path path : directoryStream) {
                String label = path.getFileName().toString().replaceFirst("[.][^.]+$", "").toUpperCase();
                if (bingoIcons.containsKey(label)) {
                    throw new IllegalArgumentException("Duplicate image label: " + label);
                }
                PdfFormXObject labelObject = loadLabel(label, gl, document, font, color);
                PdfFormXObject iconObject = loadImage(path, document);
                bingoIcons.put(label, new BingoSquare(labelObject, iconObject));
            }
        }
        return bingoIcons;
    }

    private static PdfFormXObject loadLabel(String label, GridLayout gl, PdfDocument document, PdfFont font, DeviceRgb color) {
        float labelHeight = gl.labelHeight();

        String heightSample = label.replace(" ", "");

        float textWidth = font.getWidth(label, labelHeight);
        float textHeight = font.getAscent(heightSample, labelHeight) - font.getDescent(heightSample, labelHeight);
        float textPosY = (labelHeight - textHeight) / 2 - font.getDescent(heightSample, labelHeight);

        PdfFormXObject labelObject = new PdfFormXObject(new Rectangle(textWidth, labelHeight));
        PdfCanvas canvas = new PdfCanvas(labelObject, document);
        canvas.beginText()
                .setFontAndSize(font, labelHeight)
                .setFillColor(color)
                .moveText(0, textPosY)
                .showText(label)
                .endText();

        return labelObject;
    }
}

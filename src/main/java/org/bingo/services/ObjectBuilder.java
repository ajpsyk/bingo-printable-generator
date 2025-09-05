package org.bingo.services;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;

import org.bingo.config.PageConfig;
import org.bingo.model.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ObjectBuilder {

    public static Frame getFrame(
            PdfDocument doc, PageLayout pl, Path framePath
    ) throws FileNotFoundException {

        PdfFormXObject frame = loadImage(framePath, doc);

        float frameWidthScaleFactor = pl.getPrintSafeWidth() / frame.getWidth();
        float frameHeightScaleFactor = pl.getPrintSafeHeight() / frame.getHeight();

        Transform transform = Transform.builder()
                .scaleX(frameWidthScaleFactor)
                .scaleY(frameHeightScaleFactor)
                .positionX(pl.getMarginLeft())
                .positionY(pl.getMarginTop())
                .build();

        return Frame.builder()
                .object(frame)
                .transform(transform)
                .build();
    }

    public static Header getHeader(
            PdfDocument doc, PageLayout pl, Path headerPath, PageConfig config
    ) throws FileNotFoundException {

        PdfFormXObject header = loadImage(headerPath, doc);

        float topSpacing = Conversions.inchesToPoints(config.getHeaderSpacingTopInches());
        float rightSpacing = Conversions.inchesToPoints(config.getHeaderSpacingRightInches());
        float leftSpacing = Conversions.inchesToPoints(config.getHeaderSpacingLeftInches());

        float width = pl.getPrintSafeWidth() - rightSpacing - leftSpacing;
        float height = header.getHeight();

        float scaleFactor = width / header.getWidth();
        float headerX = pl.getMarginLeft() + leftSpacing;
        float headerY = pl.getHeight() - pl.getMarginTop() - header.getHeight() * scaleFactor - topSpacing;

        Transform transform = Transform.builder()
                .scaleX(scaleFactor)
                .scaleY(scaleFactor)
                .positionX(headerX)
                .positionY(headerY)
                .build();

        return Header.builder()
                .object(header)
                .transform(transform)
                .height(height * scaleFactor)
                .build();

    }

    public static Grid getGrid (
            PdfDocument document, PageConfig config, PageLayout pl, float headerHeight, boolean dotted
    ) {
        float lineWidth = Conversions.inchesToPoints(config.getGridLineThicknessInches());
        float lineWidthOffset = lineWidth / 2;

        float gridSpacingRight = Conversions.inchesToPoints(config.getGridSpacingRightInches());
        float gridSpacingBottom = Conversions.inchesToPoints(config.getGridSpacingBottomInches());
        float gridSpacingLeft =  Conversions.inchesToPoints(config.getGridSpacingLeftInches());

        float headerSpacingTop = Conversions.inchesToPoints(config.getHeaderSpacingTopInches());
        float headerSpacingBottom = Conversions.inchesToPoints(config.getHeaderSpacingBottomInches());

        float gridWidth = pl.getPrintSafeWidth() - gridSpacingRight - gridSpacingLeft;
        float gridHeight = pl.getPrintSafeHeight() - headerSpacingTop - headerHeight - headerSpacingBottom - gridSpacingBottom;

        // iText positions lines based on their centers, so lines with variable width need to be offset
        int rows = config.getGridRowAmount();
        int columns = config.getGridColumnAmount();
        float cellWidth = (gridWidth - lineWidth) / columns;
        float cellHeight = (gridHeight - lineWidth) / rows;

        PdfFormXObject grid = new PdfFormXObject(new Rectangle(gridWidth, gridHeight));
        PdfCanvas gridCanvas = new PdfCanvas(grid, document);
        gridCanvas.setLineWidth(lineWidth);

        for (int i = 0; i < rows + 1; i++) {
            gridCanvas.moveTo(0, i * cellHeight + lineWidthOffset)
                    .lineTo(gridWidth, i * cellHeight + lineWidthOffset);
        }

        for (int j = 0; j < columns + 1; j++) {
            gridCanvas.moveTo(j * cellWidth + lineWidthOffset, lineWidthOffset)
                    .lineTo(j * cellWidth + lineWidthOffset, gridHeight - lineWidthOffset);
        }

        gridCanvas.setStrokeColor(config.getGridLineColor());
        if (dotted) {
            gridCanvas.setLineDash(3f, 3f);
        }
        gridCanvas.stroke();

        float spacingBottom = Conversions.inchesToPoints(config.getGridSpacingBottomInches());
        float spacingLeft = Conversions.inchesToPoints(config.getGridSpacingLeftInches());

        float gridX = pl.getMarginLeft() + spacingLeft;
        float gridY = pl.getMarginBottom() + spacingBottom;

        Transform transform = Transform.builder()
                .positionX(gridX)
                .positionY(gridY)
                .build();

        float cellPaddingX = cellWidth * config.getCellSpacingXRatio();

        return Grid.builder()
                .object(grid)
                .transform(transform)
                .cellWidth(cellWidth)
                .cellHeight(cellHeight)
                .cellPaddingX(cellPaddingX)
                .cellPaddingY(cellHeight * config.getCellSpacingYRatio())
                .cellGap(cellHeight * config.getCellGapRatio())
                .labelHeight(cellHeight * config.getLabelHeightRatio())
                .usableWidth(cellWidth - cellPaddingX)
                .build();


    }


    public static void addImagesAndLabelsToGrid(
            PageConfig config,
            Grid grid,
            List<String> permutation,
            Map<String, GridContent> bingoSquares,
            PdfFormXObject freeSpace,
            PdfCanvas canvas
    ) {
        int rows = config.getGridRowAmount();
        int cols = config.getGridColumnAmount();
        float cellWidth = grid.getCellWidth();
        float cellHeight = grid.getCellHeight();
        float cellPaddingY = grid.getCellPaddingY();
        float gridX = grid.getTransform().getPositionX();
        float gridY = grid.getTransform().getPositionY();
        float gap = grid.getCellGap();
        float labelHeight = grid.getLabelHeight();
        float usableWidth = grid.getUsableWidth();

        Iterator<String> permutationIterator = permutation.iterator();

        for (int row = rows - 1; row >= 0; row--) {
            for (int col = 0; col < cols; col++) {
                if (freeSpace != null && row == rows / 2 && col == cols / 2) {
                    float freeScaleX = cellWidth / freeSpace.getWidth();
                    float freeScaleY = cellHeight / freeSpace.getHeight();
                    float imageScale = Math.min(freeScaleX, freeScaleY);

                    float scaledWidth  = freeSpace.getWidth() * imageScale;
                    float scaledHeight = freeSpace.getHeight() * imageScale;

                    float freePosX = gridX + col * cellWidth + (cellWidth - scaledWidth) / 2;
                    float freePosY = gridY + row * cellHeight + (cellHeight - scaledHeight) / 2;

                    canvas.addXObjectWithTransformationMatrix(
                            freeSpace, imageScale, 0f, 0f, imageScale, freePosX, freePosY
                    );
                    continue;
                }
                String square = permutationIterator.next();
                PdfFormXObject label = bingoSquares.get(square).getLabel();
                PdfFormXObject image = bingoSquares.get(square).getIcon();


                float labelPosX = gridX + col * cellWidth + (cellWidth - label.getWidth()) / 2;
                float labelPosY = gridY + row * cellHeight + cellPaddingY;

                canvas.addXObjectWithTransformationMatrix(
                        label, 1f, 0f, 0f, 1f, labelPosX, labelPosY
                );

                float availableImageHeight = cellHeight - cellPaddingY - label.getHeight() - gap;
                float imageScaleX = usableWidth / image.getWidth();
                float imageScaleY = availableImageHeight / image.getHeight();
                float imageScale = Math.min(imageScaleX, imageScaleY);

                float scaledImageWidth  = image.getWidth() * imageScale;
                float scaledImageHeight = image.getHeight() * imageScale;

                float imagePosX = gridX + col * cellWidth + (cellWidth - scaledImageWidth) / 2;
                float imagePosY = gridY + row * cellHeight + cellPaddingY / 2 + labelHeight + gap
                        + (availableImageHeight - scaledImageHeight) / 2;

                canvas.addXObjectWithTransformationMatrix(
                        image, imageScale, 0f, 0f, imageScale, imagePosX, imagePosY
                );
            }
        }
    }

    public static void addTokensToGrid(
            PageConfig config,
            Grid grid,
            PdfFormXObject token,
            PdfCanvas canvas
    ) {
        int rows = config.getGridRowAmount();
        int cols = config.getGridColumnAmount();
        float cellWidth = grid.getCellWidth();
        float cellHeight = grid.getCellHeight();
        float cellPaddingY = grid.getCellPaddingY();
        float gridX = grid.getTransform().getPositionX();
        float gridY = grid.getTransform().getPositionY();
        float usableWidth = grid.getUsableWidth();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                float availableImageHeight = cellHeight - cellPaddingY;
                float imageScaleX = usableWidth / token.getWidth();
                float imageScaleY = availableImageHeight / token.getHeight();
                float imageScale = Math.min(imageScaleX, imageScaleY);

                float scaledImageWidth  = token.getWidth() * imageScale;
                float scaledImageHeight = token.getHeight() * imageScale;

                float imagePosX = gridX + col * cellWidth + (cellWidth - scaledImageWidth) / 2;
                float imagePosY = gridY + row * cellHeight + cellPaddingY / 2
                        + (availableImageHeight - scaledImageHeight) / 2;
                canvas.addXObjectWithTransformationMatrix(
                        token, imageScale, 0f, 0f, imageScale, imagePosX, imagePosY
                );
            }
        }
    }

    public static PdfFormXObject loadImage (Path resourcePath, PdfDocument document) throws FileNotFoundException {
        ImageData imageData;
        try {
            imageData = ImageDataFactory.create(resourcePath.toString());
        } catch (IOException e) {
            throw new FileNotFoundException("Failed to load image: " + resourcePath);
        }
        PdfFormXObject image = new PdfFormXObject(new Rectangle(imageData.getWidth(), imageData.getHeight()));
        PdfCanvas canvas = new PdfCanvas(image, document);
        canvas.addImageAt(imageData, 0, 0, false);
        return image;
    }

    public static Map<String, GridContent> loadImageDirectory(
            Path resourceDir, PdfDocument document, Grid gl, PdfFont font, DeviceRgb color
    ) throws FileNotFoundException {

        Map<String, GridContent> bingoIcons = new HashMap<>();

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(resourceDir, "*.png")) {
            for (Path path : directoryStream) {
                String label = path.getFileName().toString().replaceFirst("[.][^.]+$", "").toUpperCase();
                if (bingoIcons.containsKey(label)) {
                    throw new IllegalArgumentException("Duplicate image label: " + label);
                }
                PdfFormXObject labelObject = null;
                if (font != null) {
                    labelObject = loadLabel(label, gl, document, font, color);
                }
                PdfFormXObject iconObject = loadImage(path, document);
                bingoIcons.put(label, GridContent.builder().label(labelObject).icon(iconObject).build());
            }
        } catch (IOException ex) {
            throw new FileNotFoundException("Could not access image directory (expected PNG files): " + resourceDir);
        }
        return bingoIcons;
    }

    public static PdfFormXObject loadLabel(String label, Grid grid, PdfDocument document, PdfFont font, DeviceRgb color) {
        float labelHeight = grid.getLabelHeight();

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

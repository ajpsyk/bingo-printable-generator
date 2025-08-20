package org.bingo.generators;

import java.io.IOException;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;

import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;

import com.itextpdf.svg.converter.SvgConverter;

import org.bingo.model.ImageLoader;
import org.bingo.model.ImageShuffler;


public class BingoCardGenerator {

    public static final Path RESOURCES_DIR = Paths.get("resources");
    public static final Path FRAME_FILE = RESOURCES_DIR.resolve("Fall Nature Frame.svg");
    public static final Path HEADER_FILE = RESOURCES_DIR.resolve("Fall Nature Bingo Header.svg");
    public static final Path ICONS_DIR = Paths.get("resources/Bingo Images");

    public static final float POINTS_PER_INCH = 72f;
    public static final int rows = 5;
    public static final int cols = 5;

    public static final float pageMarginTopInches = 0.25f;
    public static final float pageMarginRightInches = 0.25f;
    public static final float pageMarginBottomInches = 0.25f;
    public static final float pageMarginLeftInches = 0.25f;

    public static final float frameInnerPaddingTopInches = 0.3f;
    public static final float frameInnerPaddingRightInches = 0.5f;
    public static final float frameInnerPaddingBottomInches = 0.5f;
    public static final float frameInnerPaddingLeftInches = 0.5f;

    public static final float lineWidthInches = 0.03f;
    public static final float gapBetweenHeaderAndGridInches = 0.1f;
    public static final int CARD_AMOUNT = 1;

    public static void generateOnePerPageFormat() throws IOException {
        try (PdfDocument pdf = new PdfDocument(new PdfWriter(OUTPUT_FILE.toString()));
             FileInputStream frameStream = new FileInputStream(FRAME_FILE.toString());
             FileInputStream headerStream = new FileInputStream(HEADER_FILE.toString())) {

            PdfPage page = pdf.addNewPage(PageSize.LETTER);
            PdfCanvas canvas = new PdfCanvas(page);

            float pageWidth = page.getPageSize().getWidth();
            float pageHeight = page.getPageSize().getHeight();

            float pageMarginTopPoints = toPoints(pageMarginTopInches);
            float pageMarginRightPoints = toPoints(pageMarginRightInches);
            float pageMarginBottomPoints = toPoints(pageMarginBottomInches);
            float pageMarginLeftPoints = toPoints(pageMarginLeftInches);

            float printSafeWidth = pageWidth - pageMarginRightPoints - pageMarginLeftPoints;
            float printSafeHeight = pageHeight - pageMarginTopPoints - pageMarginBottomPoints;

            PdfFormXObject frame = SvgConverter.convertToXObject(frameStream, pdf);
            PdfFormXObject header = SvgConverter.convertToXObject(headerStream, pdf);

            float frameWidthScaleFactor = printSafeWidth / frame.getWidth();
            float frameHeightScaleFactor = printSafeHeight / frame.getHeight();

            canvas.addXObjectWithTransformationMatrix(frame, frameWidthScaleFactor, 0f, 0f, frameHeightScaleFactor, pageMarginLeftPoints, pageMarginBottomPoints);

            float frameInnerPaddingTopPoints = toPoints(frameInnerPaddingTopInches);
            float frameInnerPaddingRightPoints = toPoints(frameInnerPaddingRightInches);
            float frameInnerPaddingBottomPoints = toPoints(frameInnerPaddingBottomInches);
            float frameInnerPaddingLeftPoints = toPoints(frameInnerPaddingLeftInches);

            float contentWidth = printSafeWidth - frameInnerPaddingRightPoints - frameInnerPaddingLeftPoints;
            float contentHeight = printSafeHeight - frameInnerPaddingTopPoints - frameInnerPaddingBottomPoints;

            float headerWidthScaleFactor = contentWidth / header.getWidth();
            float headerHeight = header.getHeight();
            float contentX = pageMarginLeftPoints + frameInnerPaddingLeftPoints;
            float headerY = pageHeight - pageMarginTopPoints - frameInnerPaddingTopPoints - headerHeight;


            canvas.addXObjectWithTransformationMatrix(header, headerWidthScaleFactor, 0f, 0f, headerWidthScaleFactor, contentX, headerY);

            float gapBetweenHeaderAndGridPoints = toPoints(gapBetweenHeaderAndGridInches);
            float lineWidthPoints = toPoints(lineWidthInches);
            float lineWidthOffset = lineWidthPoints / 2;

            float gridHeight = contentHeight - headerHeight - gapBetweenHeaderAndGridPoints;

            // iText draws lines centered on the coordinate, so we subtract lineWidth from total width/height
            // and offset each line by lineWidth / 2 to keep outermost lines fully inside the XObject
            float cellWidth = (contentWidth - lineWidthPoints) / cols;
            float cellHeight = (gridHeight - lineWidthPoints) / rows;

            float gridY = headerY - gridHeight - gapBetweenHeaderAndGridPoints;

            PdfFormXObject grid = new PdfFormXObject(new Rectangle(contentWidth, gridHeight));
            PdfCanvas gridCanvas = new PdfCanvas(grid, pdf);
            gridCanvas.setLineWidth(lineWidthPoints);
            for (int i = 0; i < rows + 1; i++) {
                gridCanvas.moveTo(0, i * cellHeight + lineWidthOffset).lineTo(contentWidth, i * cellHeight + lineWidthOffset);
            }

            for (int j = 0; j < cols + 1; j++) {
                gridCanvas.moveTo(j * cellWidth + lineWidthOffset, lineWidthOffset).lineTo(j * cellWidth + lineWidthOffset, gridHeight - lineWidthOffset);
            }
            gridCanvas.stroke();
            canvas.addXObjectAt(grid, contentX, gridY);


            // Placing images and labels. last step and then done.



            Map<String, Path> images = ImageLoader.loadImagesAndLabels(ICONS_DIR);
            Set<List<String>> shuffledImages = ImageShuffler.getUniquePermutations(new ArrayList<>(images.keySet()), CARD_AMOUNT);

            // for every list of shuffled images
            for (List<String> imageSet : shuffledImages) {
                IntStream.range(0, rows).forEach(row -> {
                    IntStream.range(0, cols).forEach(col -> {
                        if (row == 2 && col == 2) {
                            // free space
                        } else {
                            // other spaces
                            try (FileInputStream imageStream = new FileInputStream(images.get(imageSet.get(row * cols + col)).toFile())) {
                                PdfFormXObject image = SvgConverter.convertToXObject(imageStream, pdf);
                            }
                        }
                    });
                });
            }


            System.out.println("Bingo Card PDF just got created.");
        }
    }

    public void generateTwoPerPageFormat() throws IOException {}

    private static float toPoints(float inches) {
        return inches * POINTS_PER_INCH;
    }
}

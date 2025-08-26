package org.bingo.services;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import org.bingo.model.*;

public class DocumentBuilder {

    public static void buildOnePerPageBingoCards (
            DocumentConfig docConfig,
            CardConfig bingoCardConfig
    ) throws IOException {

        PdfDocument document = new PdfDocument(new PdfWriter(docConfig.getOutput().toString()));
        PageDimensions pd = LayoutCalculator.getPageDimensions(PageSize.LETTER, docConfig);

        PdfFont font = PdfFontFactory.createFont(
                docConfig.getFont().toString(),
                PdfEncodings.IDENTITY_H,
                PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED
        );

        DeviceRgb fontColor = bingoCardConfig.getLabelColor();

        int cardAmount = bingoCardConfig.getCardAmount();

        PdfFormXObject frame = XObjectLoader.loadImage(docConfig.getFrame(), document);
        PdfFormXObject header =  XObjectLoader.loadImage(docConfig.getHeader(), document);
        Map<String, BingoSquare> bingoSquares = XObjectLoader.loadImageDirectory(docConfig.getIcons(), document, font, fontColor);
        List<List<String>> permutations = BingoSquareShuffler.getUniquePermutations(
                new ArrayList<>(bingoSquares.keySet()), cardAmount
        );


        XObjectTransform frameTransform = LayoutCalculator.getFrameTransform(frame, pd);
        XObjectTransform headerTransform = LayoutCalculator.getHeaderTransform(header, bingoCardConfig, pd);
        float headerHeight = header.getHeight() * headerTransform.scaleY();
        PdfFormXObject grid = LayoutCalculator.drawGrid(document, bingoCardConfig, pd, headerHeight);
        XObjectTransform gridTransform = LayoutCalculator.getGridTransform(bingoCardConfig, pd);

        IntStream.range(0, cardAmount).forEach(i -> {
            // create new page
            PdfPage page = document.addNewPage(PageSize.LETTER);
            PdfCanvas canvas = new PdfCanvas(page);
            // add XObjects to it
            canvas.addXObjectWithTransformationMatrix(
                    frame,
                    frameTransform.scaleX(),
                    frameTransform.skewY(),
                    frameTransform.skewX(),
                    frameTransform.scaleY(),
                    frameTransform.positionX(),
                    frameTransform.positionY()
            );

            canvas.addXObjectWithTransformationMatrix(
                    header,
                    headerTransform.scaleX(),
                    headerTransform.skewY(),
                    headerTransform.skewX(),
                    headerTransform.scaleY(),
                    headerTransform.positionX(),
                    headerTransform.positionY()
            );

            canvas.addXObjectWithTransformationMatrix(
                    grid,
                    gridTransform.scaleX(),
                    gridTransform.skewY(),
                    gridTransform.skewX(),
                    gridTransform.scaleY(),
                    gridTransform.positionX(),
                    gridTransform.positionY()
            );

            // add images and labels
            List<String> permutation = permutations.get(i);
            float cellPaddingX = bingoCardConfig.getCellSpacingXRatio() * 72f;
            float cellPaddingY = bingoCardConfig.getCellSpacingYRatio() * 72f;
            float gridX = gridTransform.positionX();
            float gridY = gridTransform.positionY();
            int rows = bingoCardConfig.getRows();
            int columns = bingoCardConfig.getColumns();
            float lineWidth = bingoCardConfig.getGridLineThicknessInches() * 72f;
            float cellWidth = (grid.getWidth() - lineWidth) / columns;
            float cellHeight = (grid.getHeight() - lineWidth) / rows;

            Iterator<String> squaresIterator = permutation.iterator();

            // define absolute pixel space for each component
            float labelHeight = cellHeight * 0.08f;
            float usableWidth = cellWidth - 2 * cellPaddingX;

            for (int row = 0; row < rows; row++) {

                for (int col = 0; col < columns; col++) {


                    String square = squaresIterator.next();
                    PdfFormXObject label = bingoSquares.get(square).label();
                    PdfFormXObject image = bingoSquares.get(square).icon();

                    // === LABEL ===
                     // tweak to taste
                    float gap = 7f;

                    float labelScaleY = labelHeight / label.getHeight();

                    // apply vertical scale
                    float labelWidthScaled = label.getWidth() * labelScaleY;

                    // check if it fits in usable width
                    float labelScaleX = 1f;
                    if (labelWidthScaled > usableWidth) {
                        labelScaleX = usableWidth / labelWidthScaled; // scale horizontally down only
                    }

                    float labelScaleFinalX = labelScaleX * labelScaleY; // combine
                    float labelScaleFinalY = labelScaleY;

                    float labelPosX = gridX + col * cellWidth + (cellWidth - label.getWidth() * labelScaleFinalX) / 2;
                    float labelPosY = gridY + row * cellHeight + cellPaddingY;

                    canvas.addXObjectWithTransformationMatrix(
                            label,
                            labelScaleFinalX, 0f,
                            0f, labelScaleFinalY,
                            labelPosX, labelPosY
                    );

                    float availableImageHeight = cellHeight - 2 * cellPaddingY - label.getHeight() * labelScaleFinalY - gap;
                    float imageScaleX = usableWidth / image.getWidth();
                    float imageScaleY = availableImageHeight / image.getHeight();
                    float imageScale = Math.min(imageScaleX, imageScaleY);

                    float scaledImageWidth  = image.getWidth() * imageScale;
                    float scaledImageHeight = image.getHeight() * imageScale;

                    float imagePosX = gridX + col * cellWidth + cellPaddingX + (usableWidth - scaledImageWidth) / 2;
                    float imagePosY = gridY + row * cellHeight + cellPaddingY + labelHeight + gap
                            + (availableImageHeight - scaledImageHeight) / 2;

                    canvas.addXObjectWithTransformationMatrix(
                            image,
                            imageScale, 0f,
                            0f, imageScale,
                            imagePosX, imagePosY
                    );
                }
            }
        });


        document.close();
    }

    public static void buildTwoPerPageBingoCards (Path outputPath) throws IOException {
        PdfDocument onePerPageBingoCards = new PdfDocument(new PdfWriter(outputPath.toString()));
    }

    public static void buildInstructionsTokensCallingCards (Path outputPath) throws IOException {
        PdfDocument onePerPageBingoCards = new PdfDocument(new PdfWriter(outputPath.toString()));
    }
}

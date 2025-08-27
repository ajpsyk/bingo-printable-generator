package org.bingo.services;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;

import org.bingo.model.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LayoutCalculator {

    public static PageDimensions getPageDimensions(
            PageSize ps, DocumentConfig docConfig
    ) {
        float pageWidth = ps.getWidth();
        float pageHeight = ps.getHeight();

        float marginTop = inchesToPoints(docConfig.getMarginTopInches());
        float marginRight = inchesToPoints(docConfig.getMarginRightInches());
        float marginBottom = inchesToPoints(docConfig.getMarginBottomInches());
        float marginLeft = inchesToPoints(docConfig.getMarginLeftInches());

        float printSafeWidth = pageWidth - marginRight - marginLeft;
        float printSafeHeight = pageHeight - marginTop - marginBottom;

        return new PageDimensions(
                printSafeWidth, printSafeHeight,  marginTop, marginRight, marginBottom, marginLeft
        );
    }

    public static XObjectTransform getFrameTransform(
            PdfFormXObject frame, PageDimensions pd
    ) {
        float frameWidthScaleFactor = pd.width()/ frame.getWidth();
        float frameHeightScaleFactor = pd.height() / frame.getHeight();

        return new XObjectTransform(
                frameWidthScaleFactor, 0f, 0f, frameHeightScaleFactor, pd.marginLeft(), pd.marginTop()
        );
    }

    public static XObjectTransform getHeaderTransform(
            PdfFormXObject header, CardConfig bingoCardConfig, PageDimensions pd
    ) {
        float topSpacing = inchesToPoints(bingoCardConfig.getHeaderSpacingTopInches());
        float rightSpacing = inchesToPoints(bingoCardConfig.getHeaderSpacingRightInches());
        float leftSpacing = inchesToPoints(bingoCardConfig.getHeaderSpacingLeftInches());

        float width = pd.width() - rightSpacing - leftSpacing;
        float height = header.getHeight();

        float scaleFactor = width / header.getWidth();
        float headerX = pd.marginLeft() + leftSpacing;
        float headerY = pd.height() - topSpacing - height * scaleFactor;

        return new XObjectTransform(
                scaleFactor, 0f, 0f, scaleFactor, headerX, headerY
        );
    }

    public static GridLayout drawGrid (
            PdfDocument document, CardConfig config, PageDimensions pd, float headerHeight
    ) {
        float lineWidth = inchesToPoints(config.getGridLineThicknessInches());
        float lineWidthOffset = lineWidth / 2;

        float gridSpacingRight = inchesToPoints(config.getGridSpacingRightInches());
        float gridSpacingBottom = inchesToPoints(config.getGridSpacingBottomInches());
        float gridSpacingLeft =  inchesToPoints(config.getGridSpacingLeftInches());

        float headerSpacingTop = inchesToPoints(config.getHeaderSpacingTopInches());
        float headerSpacingBottom = inchesToPoints(config.getHeaderSpacingBottomInches());

        float gridWidth = pd.width() - gridSpacingRight - gridSpacingLeft;
        float gridHeight = pd.height() - headerSpacingTop - headerHeight - headerSpacingBottom - gridSpacingBottom;

        // iText positions lines based on their centers, so lines with variable width need to be offset
        int rows = config.getRows();
        int columns = config.getColumns();
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

        gridCanvas.setStrokeColor(config.getGridColor());
        gridCanvas.stroke();

        float cellPaddingX = cellWidth * config.getCellSpacingXRatio();
        float usableWidth = cellWidth - cellPaddingX;
        return new GridLayout(
                grid,
                cellWidth,
                cellHeight,
                cellPaddingX,
                cellHeight * config.getCellSpacingYRatio(),
                cellHeight * config.getCellGapRatio(),
                cellHeight * config.getLabelHeightRatio(),
                usableWidth
        );
    }

    public static XObjectTransform getGridTransform(
            CardConfig bingoCardConfig,
            PageDimensions pd
    ) {
        float spacingBottom = inchesToPoints(bingoCardConfig.getGridSpacingBottomInches());
        float spacingLeft = inchesToPoints(bingoCardConfig.getGridSpacingLeftInches());

        float gridX = pd.marginLeft() + spacingLeft;
        float gridY = pd.marginBottom() + spacingBottom;

        return new XObjectTransform(
                1f,
                0f,
                0f,
                1f,
                gridX,
                gridY
        );
    }

    public static void addImagesAndLabelsToGrid(
            CardConfig config,
            GridLayout gridLayout,
            XObjectTransform gridTransform,
            List<String> permutation,
            Map<String, BingoSquare> bingoSquares,
            PdfFormXObject freeSpace,
            PdfCanvas canvas
    ) {
        int rows = config.getRows();
        int cols = config.getColumns();
        float cellWidth = gridLayout.cellWidth();
        float cellHeight = gridLayout.cellHeight();
        float cellPaddingY = gridLayout.cellPaddingY();
        float gridX = gridTransform.positionX();
        float gridY = gridTransform.positionY();
        float gap = gridLayout.cellGap();
        float labelHeight = gridLayout.labelHeight();
        float usableWidth = gridLayout.usableWidth();

        Iterator<String> permutationIterator = permutation.iterator();

        for (int row = 0; row < rows; row++) {
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
                PdfFormXObject label = bingoSquares.get(square).label();
                PdfFormXObject image = bingoSquares.get(square).icon();

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


    private static float inchesToPoints(float inches) {
        return inches * 72f;
    }
}

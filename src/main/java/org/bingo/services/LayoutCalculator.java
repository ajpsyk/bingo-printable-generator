package org.bingo.services;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;

import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;

import org.bingo.model.DocumentConfig;
import org.bingo.model.CardConfig;
import org.bingo.model.PageDimensions;
import org.bingo.model.XObjectTransform;

public class LayoutCalculator {

    public static PageDimensions getPageDimensions(PageSize ps, DocumentConfig docConfig) {
        float pageWidth = ps.getWidth();
        float pageHeight = ps.getHeight();

        float marginTop = inchesToPoints(docConfig.getMarginTopInches());
        float marginRight = inchesToPoints(docConfig.getMarginRightInches());
        float marginBottom = inchesToPoints(docConfig.getMarginBottomInches());
        float marginLeft = inchesToPoints(docConfig.getMarginLeftInches());

        float printSafeWidth = pageWidth - marginRight - marginLeft;
        float printSafeHeight = pageHeight - marginTop - marginBottom;

        return new PageDimensions(printSafeWidth, printSafeHeight,  marginTop, marginRight, marginBottom, marginLeft);
    }

    public static XObjectTransform getFrameTransform(
            PdfFormXObject frame,
            PageDimensions pd
    ) {
        float frameWidthScaleFactor = pd.width()/ frame.getWidth();
        float frameHeightScaleFactor = pd.height() / frame.getHeight();
        return new XObjectTransform(
                frameWidthScaleFactor,
                0f,
                0f,
                frameHeightScaleFactor,
                pd.marginLeft(),
                pd.marginTop()
        );
    }

    public static XObjectTransform getHeaderTransform(
            PdfFormXObject header,
            CardConfig bingoCardConfig,
            PageDimensions pd
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
                scaleFactor,
                0f,
                0f,
                scaleFactor,
                headerX,
                headerY
        );
    }

    public static PdfFormXObject drawGrid (PdfDocument document, CardConfig config, PageDimensions pd, float headerHeight) {

        float gapBetweenHeaderAndGridPoints = inchesToPoints(config.getHeaderSpacingBottomInches());
        float lineWidth = inchesToPoints(config.getGridLineThicknessInches());
        float lineWidthOffset = lineWidth / 2;

        float gridSpacingRight = inchesToPoints(config.getGridSpacingRightInches());
        float gridSpacingBottom = inchesToPoints(config.getGridSpacingBottomInches());
        float gridSpacingLeft =  inchesToPoints(config.getGridSpacingLeftInches());

        float headerMarginTop = inchesToPoints(config.getHeaderSpacingTopInches());

        float gridWidth = pd.width() - gridSpacingRight - gridSpacingLeft;
        float gridHeight = pd.height() - headerMarginTop - headerHeight - gapBetweenHeaderAndGridPoints - gridSpacingBottom;


        // iText positions lines based on their centers, so lines with variable width need to be offset
        int rows = config.getRows();
        int cols = config.getColumns();
        float cellWidth = (gridWidth - lineWidth) / cols;
        float cellHeight = (gridHeight - lineWidth) / rows;


        PdfFormXObject grid = new PdfFormXObject(new Rectangle(gridWidth, gridHeight));
        PdfCanvas gridCanvas = new PdfCanvas(grid, document);
        gridCanvas.setLineWidth(lineWidth);

        for (int i = 0; i < rows + 1; i++) {
            gridCanvas.moveTo(0, i * cellHeight + lineWidthOffset)
                    .lineTo(gridWidth, i * cellHeight + lineWidthOffset);
        }

        for (int j = 0; j < cols + 1; j++) {
            gridCanvas.moveTo(j * cellWidth + lineWidthOffset, lineWidthOffset)
                    .lineTo(j * cellWidth + lineWidthOffset, gridHeight - lineWidthOffset);
        }

        gridCanvas.stroke();
        return grid;
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

    private static float inchesToPoints(float inches) {
        return inches * 72f;
    }
}

package org.bingo.boundary;

import java.io.IOException;

import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;

import com.itextpdf.svg.converter.SvgConverter;



public class BingoCardGenerator {
    public static final Path OUTPUT_DIR = Paths.get("output");
    public static final Path RESOURCES_DIR = Paths.get("resources");

    public static final Path OUTPUT_FILE = OUTPUT_DIR.resolve("BingoCard.pdf");
    public static final Path FRAME_FILE = RESOURCES_DIR.resolve("Fall Nature Frame.svg");
    public static final Path HEADER_FILE = RESOURCES_DIR.resolve("Fall Nature Bingo Header.svg");

    public static final float POINTS_PER_INCH = 72f;

    public static void main(String[] args) throws IOException {
        PdfDocument pdf = new PdfDocument(new PdfWriter(OUTPUT_FILE.toString()));
        PdfPage page =  pdf.addNewPage(PageSize.LETTER);
        PdfCanvas canvas = new PdfCanvas(page);

        float pageWidth = page.getPageSize().getWidth();
        float pageHeight = page.getPageSize().getHeight();

        float pageMarginTop = 0.25f * POINTS_PER_INCH;
        float pageMarginRight = 0.25f * POINTS_PER_INCH;
        float pageMarginBottom = 0.25f * POINTS_PER_INCH;
        float pageMarginLeft = 0.25f * POINTS_PER_INCH;

        float printSafeWidth = pageWidth - pageMarginRight - pageMarginLeft;
        float printSafeHeight = pageHeight - pageMarginTop - pageMarginBottom;

        PdfFormXObject frame = SvgConverter.convertToXObject(new FileInputStream(FRAME_FILE.toString()), pdf);
        PdfFormXObject header = SvgConverter.convertToXObject(new FileInputStream(HEADER_FILE.toString()), pdf);

        float frameWidthScaleFactor = printSafeWidth / frame.getWidth();
        float frameHeightScaleFactor = printSafeHeight / frame.getHeight();

        canvas.addXObjectWithTransformationMatrix(frame, frameWidthScaleFactor, 0f, 0f, frameHeightScaleFactor, pageMarginLeft, pageMarginBottom);

        float frameInnerPaddingTop = 0.3f * POINTS_PER_INCH;
        float frameInnerPaddingRight = 0.5f * POINTS_PER_INCH;
        float frameInnerPaddingBottom = 0.5f * POINTS_PER_INCH;
        float frameInnerPaddingLeft = 0.5f * POINTS_PER_INCH;

        float contentWidth = printSafeWidth - frameInnerPaddingRight - frameInnerPaddingLeft;
        float contentHeight = printSafeHeight - frameInnerPaddingTop - frameInnerPaddingBottom;

        float headerWidthScaleFactor = contentWidth / header.getWidth();
        float headerHeight = header.getHeight();
        float headerX = pageMarginLeft + frameInnerPaddingLeft;
        float headerY = pageHeight - pageMarginTop - frameInnerPaddingTop - headerHeight;


        canvas.addXObjectWithTransformationMatrix(header, headerWidthScaleFactor, 0f, 0f, headerWidthScaleFactor, headerX, headerY);

        float gapBetweenHeaderAndGrid = 0.1f * POINTS_PER_INCH;
        float lineWidth = 0.03f *  POINTS_PER_INCH;
        int rows = 5;
        int cols = 5;

        float gridHeight = contentHeight - headerHeight - gapBetweenHeaderAndGrid;

        // iText draws lines centered on the coordinate, so we subtract lineWidth from total width/height
        // and offset each line by lineWidth / 2 to keep outermost lines fully inside the XObject
        float cellWidth = (contentWidth - lineWidth) / cols;
        float cellHeight = (gridHeight - lineWidth) / rows;

        float gridX = pageMarginLeft + frameInnerPaddingLeft;
        float gridY = pageHeight - pageMarginTop - headerHeight - frameInnerPaddingTop - gridHeight - gapBetweenHeaderAndGrid;

        PdfFormXObject grid = new PdfFormXObject(new Rectangle(contentWidth, gridHeight));
        PdfCanvas gridCanvas = new PdfCanvas(grid, pdf);
        gridCanvas.setLineWidth(lineWidth);
        for (int i = 0; i < rows + 1; i++) {
            gridCanvas.moveTo(0, i * cellHeight + lineWidth / 2).lineTo(contentWidth, i * cellHeight + lineWidth / 2);
        }

        for (int j = 0; j < cols + 1; j++) {
            gridCanvas.moveTo(j * cellWidth + lineWidth / 2, lineWidth / 2).lineTo(j * cellWidth + lineWidth / 2, gridHeight - lineWidth / 2);
        }
        gridCanvas.stroke();
        canvas.addXObjectAt(grid, gridX, gridY);

        pdf.close();

        System.out.println("Bingo Card PDF just got created.");
    }
}

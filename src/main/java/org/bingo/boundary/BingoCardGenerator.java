package org.bingo.boundary;

import java.io.IOException;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import java.io.FileInputStream;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;

import com.itextpdf.svg.converter.SvgConverter;



public class BingoCardGenerator {
    public static final String OUTPUT_PATH = "";
    public static final String FRAME_FILE = "";

    public static void main(String[] args) throws IOException {
        PdfDocument pdf = new PdfDocument(new PdfWriter(OUTPUT_PATH));
        PdfPage page =  pdf.addNewPage(PageSize.LETTER);
        PdfCanvas canvas = new PdfCanvas(page);

        float pageWidth = page.getPageSize().getWidth();
        float pageHeight = page.getPageSize().getHeight();

        // point = 1/72 of an inch
        float pageMarginTop = 0.25f * 72f;
        float pageMarginRight = 0.25f * 72f;
        float pageMarginBottom = 0.25f * 72f;
        float pageMarginLeft = 0.25f * 72f;

        float printSafeWidth = pageWidth - pageMarginLeft - pageMarginRight;
        float printSafeHeight = pageHeight - pageMarginTop - pageMarginBottom;

        PdfFormXObject frame = SvgConverter.convertToXObject(new FileInputStream(FRAME_FILE), pdf);

        float frameWidthScaleFactor = printSafeWidth / frame.getWidth();
        float frameHeightScaleFactor = printSafeHeight / frame.getHeight();

        canvas.addXObjectWithTransformationMatrix(frame, frameWidthScaleFactor, 0f, 0f, frameHeightScaleFactor, pageMarginLeft, pageMarginBottom);

        pdf.close();

        System.out.println("Bingo Card PDF just got created.");
    }
}

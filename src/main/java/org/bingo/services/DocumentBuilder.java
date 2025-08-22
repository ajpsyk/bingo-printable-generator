package org.bingo.services;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.IOException;
import java.nio.file.Path;
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

        PdfFormXObject frame = ImageLoader.loadImage(docConfig.getFrame(), document);
        PdfFormXObject header =  ImageLoader.loadImage(docConfig.getHeader(), document);
        Map<String, BingoIcon> icons = ImageLoader.loadImageDirectory(docConfig.getIcons(), document);

        PageDimensions pd = CardLayoutCalculator.getPageDimensions(PageSize.LETTER, docConfig);
        XObjectTransform frameTransform = CardLayoutCalculator.getFrameTransform(frame, pd);
        XObjectTransform headerTransform = CardLayoutCalculator.getHeaderTransform(header, bingoCardConfig, pd);
        float headerHeight = header.getHeight() * headerTransform.scaleY();
        PdfFormXObject grid = CardLayoutCalculator.drawGrid(document, bingoCardConfig, pd, headerHeight);
        XObjectTransform gridTransform = CardLayoutCalculator.getGridTransform(bingoCardConfig, pd);

        IntStream.range(0, bingoCardConfig.getCardAmount()).forEach(i -> {
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

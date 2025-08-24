package org.bingo.services;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
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

        int cardAmount = bingoCardConfig.getCardAmount();

        PdfFormXObject frame = XObjectLoader.loadImage(docConfig.getFrame(), document);
        PdfFormXObject header =  XObjectLoader.loadImage(docConfig.getHeader(), document);
        Map<String, BingoSquare> bingoSquares = XObjectLoader.loadImageDirectory(docConfig.getIcons(), document, font);
        List<List<String>> permutations = BingoSquareShuffler.getUniquePermutations(new ArrayList<>(bingoSquares.keySet()), cardAmount);


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

            for (String icon : permutation) {
                //PdfFormXObject label = bingoSquares.get(icon).label();
                PdfFormXObject image = bingoSquares.get(icon).icon();
                /*
                canvas.addXObjectWithTransformationMatrix(
                        image,
                        imageTransform.scaleX(), // changes depending on row
                        imageTransform.skewY(),
                        imageTransform.skewX(),
                        imageTransform.scaleY(), // changes depending on column
                        imageTransform.positionX(),
                        imageTransform.positionY()
                );

                canvas.addXObjectWithTransformationMatrix(
                        label,
                        labelTransform.scaleX(),
                        labelTransform.skewY(),
                        labelTransform.skewX(),
                        labelTransform.scaleY(),
                        labelTransform.positionX(),
                        labelTransform.positionY()
                );

                */
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

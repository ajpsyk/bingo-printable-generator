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

        PdfDocument document = new PdfDocument(new PdfWriter(bingoCardConfig.getFileName().toString()));
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
        PdfFormXObject freeSpace = XObjectLoader.loadImage(docConfig.getFreeSpace(), document);
        XObjectTransform frameTransform = LayoutCalculator.getFrameTransform(frame, pd);
        XObjectTransform headerTransform = LayoutCalculator.getHeaderTransform(header, bingoCardConfig, pd);
        float headerHeight = header.getHeight() * headerTransform.scaleY();
        GridLayout gridLayout = LayoutCalculator.drawGrid(document, bingoCardConfig, pd, headerHeight);
        XObjectTransform gridTransform = LayoutCalculator.getGridTransform(bingoCardConfig, pd, 0f);
        Map<String, BingoSquare> bingoSquares = XObjectLoader.loadImageDirectory(
                docConfig.getIcons(), document, gridLayout, font, fontColor
        );
        List<List<String>> permutations = BingoSquareShuffler.getUniquePermutations(
                new ArrayList<>(bingoSquares.keySet()), cardAmount
        );

        IntStream.range(0, cardAmount).forEach(i -> {
            PdfPage page = document.addNewPage(PageSize.LETTER);
            PdfCanvas canvas = new PdfCanvas(page);
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
                    gridLayout.grid(),
                    gridTransform.scaleX(),
                    gridTransform.skewY(),
                    gridTransform.skewX(),
                    gridTransform.scaleY(),
                    gridTransform.positionX(),
                    gridTransform.positionY()
            );

            List<String> permutation = permutations.get(i);
            LayoutCalculator.addImagesAndLabelsToGrid(
                    bingoCardConfig,
                    gridLayout,
                    gridTransform,
                    permutation,
                    bingoSquares,
                    freeSpace,
                    canvas
            );
        });


        document.close();
    }

    public static void buildTwoPerPageBingoCards (
            DocumentConfig docConfig, CardConfig cardConfig
    ) throws IOException {
        PdfDocument document = new PdfDocument(new PdfWriter(cardConfig.getFileName().toString()));
        PageDimensions pd = LayoutCalculator.getLandscapePageDimensions(PageSize.LETTER.rotate(), docConfig);
        float xOffset = pd.marginLeft() + pd.marginRight() + pd.width();

        PdfFont font = PdfFontFactory.createFont(
                docConfig.getFont().toString(),
                PdfEncodings.IDENTITY_H,
                PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED
        );

        DeviceRgb fontColor = cardConfig.getLabelColor();

        int cardAmount = cardConfig.getCardAmount();

        PdfFormXObject frame = XObjectLoader.loadImage(docConfig.getFrame(), document);

        PdfFormXObject header =  XObjectLoader.loadImage(docConfig.getHeader(), document);
        PdfFormXObject freeSpace = XObjectLoader.loadImage(docConfig.getFreeSpace(), document);
        XObjectTransform frameTransform = LayoutCalculator.getFrameTransform(frame, pd);

        XObjectTransform headerTransform = LayoutCalculator.getHeaderTransform(header, cardConfig, pd);

        float headerHeight = header.getHeight() * headerTransform.scaleY();
        GridLayout gridLayout = LayoutCalculator.drawGrid(document, cardConfig, pd, headerHeight);
        XObjectTransform gridTransform = LayoutCalculator.getGridTransform(cardConfig, pd, 0);
        XObjectTransform gridTransformXOffset = LayoutCalculator.getGridTransform(cardConfig, pd, xOffset);

        Map<String, BingoSquare> bingoSquares = XObjectLoader.loadImageDirectory(
                docConfig.getIcons(), document, gridLayout, font, fontColor
        );
        List<List<String>> permutations = BingoSquareShuffler.getUniquePermutations(
                new ArrayList<>(bingoSquares.keySet()), cardAmount
        );



        IntStream.range(0, cardAmount / 2).forEach(i -> {
            PdfPage page = document.addNewPage(PageSize.LETTER.rotate());
            PdfCanvas canvas = new PdfCanvas(page);

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
                    frame,
                    frameTransform.scaleX(),
                    frameTransform.skewY(),
                    frameTransform.skewX(),
                    frameTransform.scaleY(),
                    frameTransform.positionX() + xOffset,
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
                    header,
                    headerTransform.scaleX(),
                    headerTransform.skewY(),
                    headerTransform.skewX(),
                    headerTransform.scaleY(),
                    headerTransform.positionX() + xOffset,
                    headerTransform.positionY()
            );

            canvas.addXObjectWithTransformationMatrix(
                    gridLayout.grid(),
                    gridTransform.scaleX(),
                    gridTransform.skewY(),
                    gridTransform.skewX(),
                    gridTransform.scaleY(),
                    gridTransform.positionX(),
                    gridTransform.positionY()
            );
            canvas.addXObjectWithTransformationMatrix(
                    gridLayout.grid(),
                    gridTransform.scaleX(),
                    gridTransform.skewY(),
                    gridTransform.skewX(),
                    gridTransform.scaleY(),
                    gridTransform.positionX() + xOffset,
                    gridTransform.positionY()
            );

            List<String> leftPermutation = permutations.get(i * 2);
            LayoutCalculator.addImagesAndLabelsToGrid(
                    cardConfig,
                    gridLayout,
                    gridTransform,
                    leftPermutation,
                    bingoSquares,
                    freeSpace,
                    canvas
            );

            List<String> rightPermutation = permutations.get(i * 2 + 1);
            LayoutCalculator.addImagesAndLabelsToGrid(
                    cardConfig,
                    gridLayout,
                    gridTransformXOffset,
                    rightPermutation,
                    bingoSquares,
                    freeSpace,
                    canvas
            );

        });

        document.close();
    }

    public static void buildInstructionsTokensCallingCards (Path outputPath) throws IOException {
        PdfDocument onePerPageBingoCards = new PdfDocument(new PdfWriter(outputPath.toString()));
    }
}

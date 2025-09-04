package org.bingo.services;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.IntStream;

import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import org.bingo.model.*;

public class DocumentBuilder {

    public static void buildOnePerPageBingoCards (
            DocumentConfig docConfig,
            PageConfig bingoPageConfig
    ) throws IOException {

        PdfDocument document = new PdfDocument(new PdfWriter(docConfig.getOutputPath().toString()));
        PageDimensions pd = LayoutCalculator.getPageDimensions(docConfig.getPageSize(), docConfig);
        AssetPaths assets = docConfig.getAssets();

        int cardAmount = bingoPageConfig.getCopies();

        PdfFormXObject frame = XObjectLoader.loadImage(assets.getFramePath(), document);
        PdfFormXObject header =  XObjectLoader.loadImage(assets.getHeaderPath(), document);
        PdfFormXObject freeSpace = XObjectLoader.loadImage(assets.getFreeSpacePath(), document);

        XObjectTransform frameTransform = LayoutCalculator.getFrameTransform(frame, pd);
        XObjectTransform headerTransform = LayoutCalculator.getHeaderTransform(header, bingoPageConfig, pd);
        float headerHeight = header.getHeight() * headerTransform.scaleY();
        GridLayout gridLayout = LayoutCalculator.drawGrid(document, bingoPageConfig, pd, headerHeight, false);
        XObjectTransform gridTransform = LayoutCalculator.getGridTransform(bingoPageConfig, pd, 0f);
        Map<String, GridContent> bingoSquares = XObjectLoader.loadImageDirectory(
                assets.getIconsPath(), document, gridLayout, createFont(assets.getFontPath()), docConfig.getFontColor()
        );
        List<List<String>> permutations = BingoSquareShuffler.getUniquePermutations(
                new ArrayList<>(bingoSquares.keySet()), cardAmount, 1
        );

        IntStream.range(0, cardAmount).forEach(i -> {
            PdfPage page = document.addNewPage(PageSize.LETTER);
            PdfCanvas canvas = new PdfCanvas(page);

            addToDocument(canvas, frame, frameTransform, 0f);
            addToDocument(canvas, header, headerTransform, 0f);
            addToDocument(canvas, gridLayout.grid(), gridTransform, 0f);

            List<String> permutation = permutations.get(i);
            LayoutCalculator.addImagesAndLabelsToGrid(
                    bingoPageConfig, gridLayout, gridTransform, permutation, bingoSquares, freeSpace, canvas
            );
        });


        document.close();
    }

    public static void buildTwoPerPageBingoCards (
            DocumentConfig docConfig, PageConfig pageConfig
    ) throws IOException {
        PdfDocument document = new PdfDocument(new PdfWriter(docConfig.getOutputPath().toString()));
        PageDimensions pd = LayoutCalculator.getLandscapePageDimensions(PageSize.LETTER.rotate(), docConfig);
        float xOffset = pd.marginLeft() + pd.marginRight() + pd.width();
        AssetPaths assets = docConfig.getAssets();

        int cardAmount = pageConfig.getCopies();

        PdfFormXObject frame = XObjectLoader.loadImage(assets.getFramePath(), document);

        PdfFormXObject header =  XObjectLoader.loadImage(assets.getHeaderPath(), document);
        PdfFormXObject freeSpace = XObjectLoader.loadImage(assets.getFreeSpacePath(), document);
        XObjectTransform frameTransform = LayoutCalculator.getFrameTransform(frame, pd);

        XObjectTransform headerTransform = LayoutCalculator.getHeaderTransform(header, pageConfig, pd);

        float headerHeight = header.getHeight() * headerTransform.scaleY();
        GridLayout gridLayout = LayoutCalculator.drawGrid(document, pageConfig, pd, headerHeight, false);
        XObjectTransform gridTransform = LayoutCalculator.getGridTransform(pageConfig, pd, 0);
        XObjectTransform gridTransformXOffset = LayoutCalculator.getGridTransform(pageConfig, pd, xOffset);

        Map<String, GridContent> bingoSquares = XObjectLoader.loadImageDirectory(
                assets.getIconsPath(), document, gridLayout, createFont(assets.getFontPath()), docConfig.getFontColor()
        );
        List<List<String>> permutations = BingoSquareShuffler.getUniquePermutations(
                new ArrayList<>(bingoSquares.keySet()), cardAmount, 1
        );
        IntStream.range(0, cardAmount / 2).forEach(i -> {
            PdfPage page = document.addNewPage(PageSize.LETTER.rotate());
            PdfCanvas canvas = new PdfCanvas(page);

            addToDocument(canvas, frame, frameTransform, 0f);
            addToDocument(canvas, frame, frameTransform, xOffset);

            addToDocument(canvas, header, headerTransform, 0f);
            addToDocument(canvas, header, headerTransform, xOffset);

            addToDocument(canvas, gridLayout.grid(), gridTransform, 0f);
            addToDocument(canvas, gridLayout.grid(), gridTransform, xOffset);


            List<String> leftPermutation = permutations.get(i * 2);
            LayoutCalculator.addImagesAndLabelsToGrid(
                    pageConfig, gridLayout, gridTransform, leftPermutation, bingoSquares, freeSpace, canvas
            );

            List<String> rightPermutation = permutations.get(i * 2 + 1);
            LayoutCalculator.addImagesAndLabelsToGrid(
                    pageConfig, gridLayout, gridTransformXOffset, rightPermutation, bingoSquares, freeSpace, canvas
            );

        });

        document.close();
    }

    public static void buildInstructionsTokensCallingCards (
            DocumentConfig docConfig,
            PageConfig tokenConfig,
            PageConfig callingCardsConfig,
            PageConfig multiCallingCardsConfig,
            PageConfig multiCallingLastCardsConfig
    ) throws IOException {

        // Tokens
        AssetPaths assets = docConfig.getAssets();
        PdfDocument document = new PdfDocument(
                new PdfReader(assets.getInstructionsPath().toString()),
                new PdfWriter(docConfig.getOutputPath().toString())
        );
        PageDimensions pd = LayoutCalculator.getPageDimensions(PageSize.LETTER, docConfig);
        PdfPage page = document.addNewPage(PageSize.LETTER);
        PdfCanvas canvas = new PdfCanvas(page);

        PdfFormXObject scissors = XObjectLoader.loadImage(assets.getScissorsIconPath(), document);
        XObjectTransform scissorsTransform = LayoutCalculator.getHeaderTransform(scissors, tokenConfig, pd);
        addToDocument(canvas, scissors, scissorsTransform, 0f);
        float scissorsHeight = scissors.getHeight() * scissorsTransform.scaleY();
        GridLayout gridLayout = LayoutCalculator.drawGrid(document, tokenConfig, pd, scissorsHeight, true);
        XObjectTransform gridTransform = LayoutCalculator.getGridTransform(tokenConfig, pd, 0);
        addToDocument(canvas, gridLayout.grid(), gridTransform, 0f);

        PdfFormXObject token = XObjectLoader.loadImage(assets.getTokenPath(), document);

        LayoutCalculator.addTokensToGrid(
                tokenConfig,
                gridLayout,
                gridTransform,
                token,
                canvas
        );


        // Calling Cards Single
        PdfPage page2 = document.addNewPage(PageSize.LETTER);
        PdfCanvas canvas2 = new PdfCanvas(page2);

        PdfFormXObject callingCardHeader = XObjectLoader.loadImage(assets.getCallingCardsHeaderPath(), document);
        XObjectTransform callingCardHeaderTransform = LayoutCalculator.getHeaderTransform(
                callingCardHeader, callingCardsConfig, pd
        );
        addToDocument(canvas2, callingCardHeader, callingCardHeaderTransform, 0f);

        float headerHeight = callingCardHeader.getHeight() * callingCardHeaderTransform.scaleY();
        GridLayout ccGridLayout = LayoutCalculator.drawGrid(
                document, callingCardsConfig, pd, headerHeight, false
        );
        XObjectTransform ccGridTransform = LayoutCalculator.getGridTransform(callingCardsConfig, pd, 0);
        addToDocument(canvas2, ccGridLayout.grid(), ccGridTransform, 0f);

        Map<String, GridContent> ccSquares = XObjectLoader.loadImageDirectory(
                assets.getIconsPath(), document, ccGridLayout, createFont(assets.getFontPath()), docConfig.getFontColor()
        );
        List<String> labelsInOrder = new ArrayList<>(ccSquares.keySet());
        labelsInOrder.sort(Comparator.naturalOrder());
        LayoutCalculator.addImagesAndLabelsToGrid(
                callingCardsConfig, ccGridLayout, ccGridTransform, labelsInOrder, ccSquares, null, canvas2
        );

        // Calling Cards Multi
        GridLayout multiGridLayout = LayoutCalculator.drawGrid(
                document, multiCallingCardsConfig, pd, scissorsHeight, true
        );
        XObjectTransform multiGridTransform = LayoutCalculator.getGridTransform(multiCallingCardsConfig, pd, 0);
        Map<String, GridContent> multiSquares = XObjectLoader.loadImageDirectory(
                assets.getIconsPath(), document, multiGridLayout, createFont(assets.getFontPath()), docConfig.getFontColor()
        );
        for (int i = 0; i < 7; i++) {
            PdfPage page3 = document.addNewPage(PageSize.LETTER);
            PdfCanvas canvas3 = new PdfCanvas(page3);

            addToDocument(canvas3, scissors, scissorsTransform, 0f);
            addToDocument(canvas3, multiGridLayout.grid(), multiGridTransform, 0f);
            LayoutCalculator.addImagesAndLabelsToGrid(
                    multiCallingCardsConfig,
                    multiGridLayout,
                    multiGridTransform,
                    labelsInOrder.subList(i * 4, i * 4 + 4),
                    multiSquares,
                    null,
                    canvas3
            );
        }
        GridLayout multiGridLayoutLastPage =  LayoutCalculator.drawGrid(
                document, multiCallingLastCardsConfig, pd, scissorsHeight, true
        );
        XObjectTransform multiGridLastTransform = LayoutCalculator.getGridTransform(
                multiCallingLastCardsConfig, pd, 0
        );
        PdfPage page4 = document.addNewPage(PageSize.LETTER);
        PdfCanvas canvas4 = new PdfCanvas(page4);
        addToDocument(canvas4, scissors, scissorsTransform, 0f);
        addToDocument(canvas4, multiGridLayoutLastPage.grid(), multiGridLastTransform, 0f);
        LayoutCalculator.addImagesAndLabelsToGrid(
                multiCallingLastCardsConfig,
                multiGridLayoutLastPage,
                multiGridLastTransform,
                labelsInOrder.subList(28, 30),
                multiSquares,
                null,
                canvas4
        );

        document.close();
    }

    public static void addToDocument(
            PdfCanvas canvas, PdfFormXObject object, XObjectTransform transform, float xOffset
    ) {
        canvas.addXObjectWithTransformationMatrix(
                object,
                transform.scaleX(),
                transform.skewY(), transform.skewX(),
                transform.scaleY(),
                transform.positionX() + xOffset, transform.positionY()
        );
    }

    public static PdfFont createFont(Path fontPath) throws IOException {
        return PdfFontFactory.createFont(
                fontPath.toString(),
                PdfEncodings.IDENTITY_H,
                PdfFontFactory.EmbeddingStrategy.FORCE_EMBEDDED
        );
    }
}
